package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ExpirationTimeChecker createBean(RequestRepository requestRepository, RabbitTemplate template) {
        return new ExpirationTimeChecker(requestRepository, template);
    }
}
