package com.swagger.springwebfluxdemo.repository;

import com.swagger.springwebfluxdemo.dto.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CustomerDao {

    public static void sleepExecution(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customer> getCustomers() {
        return IntStream.rangeClosed(1,10)
                .peek(CustomerDao::sleepExecution)
                //.peek(i-> System.out.println(i))
                .peek(System.out::println)
                .mapToObj(i-> new Customer(i, "Customer" + i))
                .collect(Collectors.toList());
    }

    public Flux<Customer> getCustomersStream() {
        return Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .map(i-> new Customer(i, "Customer" + i));
    }

    public Flux<Customer> getCustomerList() {
        return Flux.range(1,10)
                .doOnNext(System.out::println)
                .map(i-> new Customer(i, "Customer" + i));
    }
}
