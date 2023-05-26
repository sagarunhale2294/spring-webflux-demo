package com.swagger.springwebfluxdemo;

import com.swagger.springwebfluxdemo.controller.ProductController;
import com.swagger.springwebfluxdemo.dto.ProductDto;
import com.swagger.springwebfluxdemo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ProductService productService;

    @Test
    public void createProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "Mobile", 2, 30));
        when(productService.createProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post()
                .uri("/products/")
                .body(productDtoMono, ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "Mobile", 2, 15000), new ProductDto("102", "TV", 1, 50000), new ProductDto("103", "Refrigerator", 1, 20000));
        when(productService.getAllProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products/")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("101", "Mobile", 2, 15000))
                .expectNext(new ProductDto("102", "TV", 1, 50000))
                .expectNextMatches(productDto -> productDto.getName().equals("Refrigerator"))
                .verifyComplete();
    }

    @Test
    public void getProductDetailsTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "Mobile", 2, 15000));
        when(productService.getProductDetails(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products/101")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(productDto -> productDto.getName().equals("Mobile"))
                .verifyComplete();
    }

    @Test
    public void updateProductTest() {
       Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "Mobile", 2, 15000));
       when(productService.updateProduct(any(), anyString())).thenReturn(productDtoMono);

       Flux<ProductDto> responseBody = webTestClient.put()
               .uri("/products/update/101")
               .body(productDtoMono, ProductDto.class)
               .exchange()
               .expectStatus().isOk()
               .returnResult(ProductDto.class)
               .getResponseBody();

       StepVerifier.create(responseBody)
               .expectSubscription()
               .expectNextMatches(productDto -> productDto.getName().equals("Mobile"))
               .verifyComplete();
    }

    @Test
    public void deleteProductTest() {
        given(productService.deleteProduct(any())).willReturn(Mono.empty());
        webTestClient.delete()
                .uri("/products/101")
                .exchange()
                .expectStatus().isOk();
    }
}
