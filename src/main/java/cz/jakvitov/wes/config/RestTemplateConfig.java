package cz.jakvitov.wes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfig {

    @Bean
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

}
