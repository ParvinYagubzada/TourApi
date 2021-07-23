package az.code.tourapi.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String REQUEST_QUEUE = "offerQueue";
    public static final String REQUEST_EXCHANGE = "offerExchange";
    public static final String REQUEST_KEY = "offerKey";

    @Bean(name = REQUEST_QUEUE)
    public Queue queueRequest() {
        return new Queue(REQUEST_QUEUE);
    }

    @Bean(name = REQUEST_EXCHANGE)
    public TopicExchange exchangeRequest() {
        return new TopicExchange(REQUEST_EXCHANGE);
    }

    @Bean
    public Binding bindingRequest(@Qualifier(REQUEST_QUEUE) Queue queue,
                                  @Qualifier(REQUEST_EXCHANGE) TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(REQUEST_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory, MessageConverter converter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
