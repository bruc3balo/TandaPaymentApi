package com.tanda.payment_api.globals;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GlobalBeans {

    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }
}
