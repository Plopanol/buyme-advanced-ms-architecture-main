package com.improvemyskills.customerservice.web;

import com.improvemyskills.customerservice.entity.Customer;
import com.improvemyskills.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers/{id}")
    ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(
                customerService.getCustomer(id).orElse(null)
        );
    }
    @GetMapping(path = "/customers")
    ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    @PostMapping("/customers")
    ResponseEntity<Customer> postCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok(
                customerService.save(customer)
        );
    }
}
