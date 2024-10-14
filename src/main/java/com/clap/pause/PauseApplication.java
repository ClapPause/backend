package com.clap.pause;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({JwtProperties.class, ImageProperties.class})
@EnableScheduling
public class PauseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PauseApplication.class, args);
    }
}
