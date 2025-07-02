package com.improvemyskills.customerservice.web;

import com.improvemyskills.customerservice.entity.Customer;
import com.improvemyskills.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/auth")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/auth2")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Authentication authentication2(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(
                customerService.getCustomer(id).orElse(null)
        );
    }
    @GetMapping(path = "/customers")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<Customer> postCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok(
                customerService.save(customer)
        );
    }
}
