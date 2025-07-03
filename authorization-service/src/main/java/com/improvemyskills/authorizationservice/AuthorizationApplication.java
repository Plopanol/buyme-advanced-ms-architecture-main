package com.improvemyskills.authorizationservice;

import com.improvemyskills.authorizationservice.entity.Permission;
import com.improvemyskills.authorizationservice.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(PermissionRepository permissionRepository){
        return args -> {
            permissionRepository.saveAll(
                    List.of(
                            Permission.builder().username("user1").action("GET").resource("CUSTOMER-SERVICE").build(),
                            Permission.builder().username("admin").action("POST").resource("CUSTOMER-SERVICE").build(),
                            Permission.builder().username("admin").action("GET").resource("CUSTOMER-SERVICE").build(),
                            Permission.builder().username("admin").action("POST").resource("ORDER-SERVICE").build(),
                            Permission.builder().username("admin").action("GET").resource("ORDER-SERVICE").build(),
                            Permission.builder().username("user1").action("GET").resource("ORDER-SERVICE").build(),
                            Permission.builder().username("admin").action("POST").resource("PRODUCT-SERVICE").build(),
                            Permission.builder().username("admin").action("GET").resource("PRODUCT-SERVICE").build(),
                            Permission.builder().username("user1").action("GET").resource("PRODUCT-SERVICE").build()
                    )
            );
        };
    }
}
