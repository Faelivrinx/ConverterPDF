package converter.config;

import convert.ConverterFactory;
import convert.impl.ConverterFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertConfig {

    @Bean
    public ConverterFactory converterFactory(){
        return new ConverterFactoryImpl();
    }

}
