package cz.jakvitov.wes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public SchedulingConfig createSpringConfig(){
        return new SchedulingConfig();
    }

}
