package com.swagger.springwebfluxdemo.service;

import com.swagger.springwebfluxdemo.repository.CustomerDao;
import com.swagger.springwebfluxdemo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    public List<Customer> getAllCustomers() {
        long start = System.currentTimeMillis();
        List<Customer> customerList = customerDao.getCustomers();
        long end = System.currentTimeMillis();
        System.out.println("Time required to complete processing=" + (end-start));
        return customerList;
    }

    public Flux<Customer> getAllCustomersStream() {
        long start = System.currentTimeMillis();
        Flux<Customer> customerList = customerDao.getCustomersStream();
        long end = System.currentTimeMillis();
        System.out.println("Time required to complete processing=" + (end-start));
        return customerList;
    }
}
