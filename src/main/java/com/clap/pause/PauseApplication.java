package com.clap.pause;

import com.clap.pause.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({JwtProperties.class})
public class PauseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PauseApplication.class, args);
    }
}
