package com.swagger.springwebfluxdemo.handlers;

import com.swagger.springwebfluxdemo.repository.CustomerDao;
import com.swagger.springwebfluxdemo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {

    @Autowired
    CustomerDao customerDao;

    public Mono<ServerResponse> loadCustomers(ServerRequest serverRequest) {
        return ServerResponse.ok().body(customerDao.getCustomerList(), Customer.class);
    }

    public Mono<ServerResponse> loadCustomersStream(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(customerDao.getCustomersStream(), Customer.class);
    }

    public  Mono<ServerResponse> findCustomer(ServerRequest request) {
        int customerId = Integer.valueOf(request.pathVariable("customerId"));
        // Mono<Customer> mono = customerDao.getCustomerList().filter(customer -> customer.getId()== customerId).take(1).single();
        Mono<Customer> customerMono = customerDao.getCustomerList().filter(customer -> customer.getId()== customerId).next();
        return ServerResponse.ok().body(customerMono, Customer.class);
    }

    public Mono<ServerResponse> createCustomer(ServerRequest request) {
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<String> response = customerMono.map(customer -> customer.getId() + ":" + customer.getName());
        return ServerResponse.ok().body(response, String.class);
    }
}
