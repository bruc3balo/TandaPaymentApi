package com.tanda.payment_api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
@EnableJpaAuditing
public class PaymentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApiApplication.class, args);
    }

    @PostConstruct
    public void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
    }

}
