package com.example.cachedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CashdeomApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashdeomApplication.class, args);
    }

}
