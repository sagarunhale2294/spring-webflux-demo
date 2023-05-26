package com.swagger.springwebfluxdemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoFluxTest {

    @Test
    public  void monoTest() {
        Mono<?> stringMono = Mono.just("Hello")
                .then(Mono.error(new Exception("this is exception")))
                .log();
        stringMono.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }

    @Test
    public void fluxTest() {
        Flux<String> fluxString = Flux.just("Spring", "boot", "java")
                .concatWith(Flux.just("ruby"))
                //.concatWith(Flux.error(new Exception("Exception in flux")))
                .concatWith(Flux.just("python")).log();
        // fluxString.subscribe(System.out::println, (e)-> System.out.println(e.getMessage()));
        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .expectNext("boot")
                .expectNext("java")
                .expectNextMatches(p->p.matches("ruby"))
                .expectNext("python")
                .expectComplete()
                .verify();
    }
}
