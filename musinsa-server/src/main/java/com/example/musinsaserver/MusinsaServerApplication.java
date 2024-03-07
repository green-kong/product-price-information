package com.example.musinsaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MusinsaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusinsaServerApplication.class, args);
    }

}
