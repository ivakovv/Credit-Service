package com.credit.credit.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class HelloWorldControllerTest {

    @Test
    public void testSayHello(){
        HelloWorldController helloWorldController = new HelloWorldController();

        ResponseEntity<String> output = helloWorldController.sayHello();

        Assertions.assertEquals(ResponseEntity.ok("Hello World!"), output);
    }
}
