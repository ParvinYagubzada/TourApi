package az.code.tourapi.configurations;

import az.code.tourapi.security.TokenInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@EnableTransactionManagement
public class BaseConfig implements WebMvcConfigurer {
    final
    TokenInterceptor productServiceInterceptor;

    public BaseConfig(TokenInterceptor productServiceInterceptor) {
        this.productServiceInterceptor = productServiceInterceptor;
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productServiceInterceptor).excludePathPatterns("/api/v1/auth/*");
    }
}
