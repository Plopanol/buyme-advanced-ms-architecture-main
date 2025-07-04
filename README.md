# buyme-advanced-ms-architecture

Github du formateur : https://github.com/ImproveMySkills/buyme-advanced-ms-architecture

## URL
- localhost:8761 -> Discovery
- localhost:8080 -> Gateway

# Configuration

## Desactiver actuator endpoint inutile en prod !!

https://docs.spring.io/spring-boot/reference/actuator/endpoints.html

N'ajouter que ceux utiles (metrique, refresh, etc)
```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### Refraichiseement auto
- Ajouter actuator dans le pom
- Activer le endpoint specifique d'actuator :
  ```yml
    endpoint:
      refresh:
        enabled: true
  ```
- Ajouter sur la configuration `@Component` par exemple mais pas sur `SecurityConfig` !
  ```java
    @Configuration
    @RefreshScope
    public class Config {
    
    }
  ```
## Configuration centralisée

### Par fichier
La configuration centralisée est fait par fichier.
Pour activer la configuration par fichier au lieu de git, voici les modifications à effectuer :
- Ajout du profile native
  ```yml
    profiles:
      active: native
  ```
- Ajout du chemin de la configuration
  ```yml
    cloud:
      config:
        server:
          native:
            search-locations: file:///D:\Dev\Ici\buyme-advanced-ms-architecture-main\config-repo-main
  ```
### Par git
Pour remettre celle avec git :
- Supprimer le `profile native`
- Remettre git a la place de native
  ```yml
    cloud:
      config:
        server:
          git:
            uri: https://github.com/ImproveMySkills/config-repo
  ```
Modifier le chemin dans le fichier `config-service/.../application.yml` pour accèder au repertoire `config-repo-main`

### Verifier la config
Acces direct aux json de la configuration par le micro-service `config-service`:
- `localhost:PORT_DE_CONFIG_SERVICE/application/profil` (default, dev, prod)

## Insomnium
Les tests sont fait par Insomnium, le workspace se trouve dans le repertoire `insomnium` du projet.

# Sécurité

## Keycloak

Intallation : https://www.keycloak.org/downloads

### Lancer Keycloak
`launch_keycloak.bat` qui contient juste : 
```cmd
kc.bat start-dev --http-port=7080
```

### Lister endpoints
Dans `Realms Settings` -> `OpenID Endpoint Configuration`

URL pour créer un token keycloak (Attention au nom du royaume a bien mettre)
http://localhost:7080/realms/buyme-realm/protocol/openid-connect/token
(Voir insomnium)

Lire un jwt : https://jwt.io/

## Activer Spring Security
Dans chaque micro-service (hors technique pour l'instant)

- Ajouter dans le `pom.xml` : 
  ```xml   
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
  ```
- Ajouter dans `application.yml` (Bien modifier le port et le nom du realms avec les votres)
  ```yml
    security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:7080/realms/buyme-realm
          jwk-set-uri: http://localhost:7080/realms/buyme-realm/protocol/openid-connect/certs
  ```
- Création de la config `SecurityConfig.java`
  ```java
    @Configuration
    @EnableWebSecurity
    // Ajouter pour activer les annotations preAuthorize dans les controllers (Activer par défaut avec Spring)
    @EnableMethodSecurity(prePostEnabled = true)
    public class SecurityConfig {
    
        private final JwtAuthConverter jwtAuthConverter;
    
        public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
            this.jwtAuthConverter = jwtAuthConverter;
        }
    
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .cors(Customizer.withDefaults())
                    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(csrf -> csrf.disable())
                    .headers(h -> h.frameOptions(fo -> fo.disable()))
                    .authorizeHttpRequests(ar -> ar.requestMatchers("/h2-console/**","/swagger-ui.html","/v3/**","/swagger-ui/**").permitAll())
                    //.authorizeHttpRequests(ar -> ar.requestMatchers("/customers").hasAnyAuthority("ADMIN"))
                    .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                    //.oauth2ResourceServer(o2 -> o2.jwt(Customizer.withDefaults()))
                    // Ajout du converter jwt token Keycloak pour deplacer les info de Keycloak dans la partie lisible de SpringSecu
                    .oauth2ResourceServer(o2 -> o2.jwt(
                            jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)
                    ))
                    .build();
        }
    }
  ```
- Création du converter jwt Keycloak -> Spring
  ```java
    @Component
    public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        private final JwtGrantedAuthoritiesConverter
        jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        
        @Override
        public AbstractAuthenticationToken convert(Jwt source) {
    
            // Le jwtGrantedAuthoritiesConverter va lire le scope du token, pour recuperer les infos
            // Le extractResourceRoles va extraire les infos mis dans realm_access les informations des roles
            // Puis ca les concatènes
            Collection<GrantedAuthority> authorities = Stream.concat(
                    jwtGrantedAuthoritiesConverter.convert(source).stream(),
                    extractResourceRoles((source)).stream()
            ).collect(Collectors.toSet());
    
            return new JwtAuthenticationToken(source, authorities,source.getClaim("preferred_username"));
        }
    
        private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
            Map<String , Object> realmAccess;
            Collection<String> roles;
            if(jwt.getClaim("realm_access")==null){
                return Set.of();
            }
            realmAccess = jwt.getClaim("realm_access");
            roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
        }
    }
  ```

- Ajout de l'annotation `PreAuthorize` sur le endpoint à securiser
  ```java
    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }
  ```


## Propagation du token entre microservice par appel avec Feign
Voir : `FeignInterceptor.java`

```java
@Component
public class FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String jwtAccessToken = jwtAuthenticationToken.getToken().getTokenValue();
		requestTemplate.header("Authorization", "Bearer " + jwtAccessToken);
	}
}
```

## Vault

Installation : https://developer.hashicorp.com/vault/install
`launch_vault.bat`
Modifier le chemin pour votre installation et le root-token-id :
```cmd 
D:\Dev\Outils\vault_1.20.0_windows_386\vault.exe server -dev -dev-root-token-id cwIiwidHlwIjoiQmVhcmVyIiwiYXpwI -dev-tls
``` 

Dans la ligne de commande en dessous il y aura les lignes :

```cmd
Unseal Key: BZgtaVs4tQTDTCWL4XGJyzFBPks3PrcEHavZ0JmJf70=
Root Token: hvs.NS0YilS5Dm7qQqataRH4HoTC
```

URL par défaut : https://127.0.0.1:8200
Une UI est disponible.

# Webflux / MVC
Uniquement la Gateway est en webflux

MVC
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

WEBFLUX
```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway-server-webflux</artifactId>
        </dependency>
```

# REsilience4J

Ajouter pom : 
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

Order-Service -> CustomerClient

```java
@CircuitBreaker(name = "customerservice", fallbackMethod = "getDefaultCustomer")
ResponseEntity<Customer> getCustomerById(@PathVariable Long id);

default ResponseEntity<Customer> getDefaultCustomer(Long id, Exception exception){
  return ResponseEntity.ok(Customer.builder()
          .firstName("UNKNOWN")
          .lastName("UNKNOWN")
          .email("UNKNOWN@improvemyskills.com")
          .build());
}
```

Permet de faire une réponse par défaut quand on appel un autre microservice.
On peut personnaliser la réponse.

# Loadbalancing

La Gateway pour le load balancing doit etre gerer à l'ancienne, si 2 instance gateway.
Pour les autres c'est discovery qui va gerer.

# A mettre en place :

Pour nous on mettera la securité : 
- sur la gateway uniquement l'autentification
- chaque microservice avec autentification/autorization avec feign voir AuthorizationService de Customer-Service
- Utilisation du vault pour docker

Dans chaque microservice :
- FeignInterceptor
- SecurityConfig
- Lister uniquement les actuators utilisées
- RefreshScope ??
- @PreAuthorize("@authorizationService.hasPermission(authentication, 'GET', 'CUSTOMER-SERVICE')")
- Docker : RUN addgroup -S spring && adduser -S spring -G spring

# Creation SSL Key :
openssl genrsa -out server.key
openssl req -new -key server.key -out server.csr
Mettre en nom keycloak
openssl x509 -req -in server.csr -signkey server.key -days 365 -out server.crt

## Dans chaque Dockerfile

Ajouter le certificat 
COPY keyssh/server.crt server.crt
RUN keytool -import -trustcacerts -alias keycloak-cert -file server.crt -keystore /app/truststore.jks -storepass changeit -noprompt

+ "-Djavax.net.ssl.trustStore=/app/truststore.jks", "-Djavax.net.ssl.trustStorePassword=changeit"

## Curl a partir d'un microservice pour ne pas avoir l'erreur iss
https://www.cyberciti.biz/faq/how-to-install-curl-on-alpine-linux/

curl --request POST \
--url https://keycloak:8443/realms/buyme-realm/protocol/openid-connect/token \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'User-Agent: insomnium/0.2.3-a' \
--data grant_type=password \
--data client_id=buyme-ms-client \
--data username=user1 \
--data password=user1 \
-k <--- Permet de s'en balek HTTPS


Inutiles mais pour memoire 
#keytool -genkeypair -alias buymecert -keypass pass123 -validity 365 -storepass changeit -keyalg RSA
#keytool -certreq -keyalg RSA -alias buymecert -file buymecert.csr
#keytool -export -alias buymecert -file buymecert.cer

## Autre solution en complement de l'autorizationService
Redis
StickySession
Cashd