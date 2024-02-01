package com.rd.rockpaperscissorstelnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RockPaperScissorsTelnetApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockPaperScissorsTelnetApplication.class, args);
    }

}
