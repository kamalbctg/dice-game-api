package com.game.dice.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DiceBoardApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiceBoardApplication.class, args);
    }
}

