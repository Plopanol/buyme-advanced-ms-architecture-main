package com.improvemyskills.customerservice;

import com.improvemyskills.customerservice.entity.Customer;
import com.improvemyskills.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.saveAll(
                    List.of(
                            Customer.builder().firstName("Ahmadou").lastName("ABOUBAKAR").email("ahmadou.aboubakar@improvemyskills.com").build(),
                            Customer.builder().firstName("King").lastName("Tchala").email("king.tchala@improvemyskills.com").build(),
                            Customer.builder().firstName("Tony").lastName("STARK").email("tony.stark@improvemyskills.com").build(),
                            Customer.builder().firstName("Fatou").lastName("DIALLO").email("fatou.diallo@example.com").build(),
                            Customer.builder().firstName("Jean").lastName("DUPONT").email("jean.dupont@monmail.fr").build(),
                            Customer.builder().firstName("Amina").lastName("BENALI").email("amina.benali@startuphub.io").build(),
                            Customer.builder().firstName("Mohamed").lastName("KAMARA").email("mohamed.kamara@techmail.com").build(),
                            Customer.builder().firstName("Lucie").lastName("MARTIN").email("lucie.martin@webmail.com").build(),
                            Customer.builder().firstName("Ousmane").lastName("SOW").email("ousmane.sow@digitalafrica.org").build(),
                            Customer.builder().firstName("Chloé").lastName("LEFEBVRE").email("chloe.lefebvre@cloudmail.net").build(),
                            Customer.builder().firstName("Yacine").lastName("TOURE").email("yacine.toure@devmail.org").build(),
                            Customer.builder().firstName("Nina").lastName("KOUASSI").email("nina.kouassi@educonnect.com").build()

                    )
            );
        };
    }
}
