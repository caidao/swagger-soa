package com.paner.swagger.soa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2JacksonModule;

/**
 * Created by www-data on 17/2/23.
 */

@Configuration
@Import({ SpringfoxSoaServiceConfiguration.class, SwaggerCommonConfiguration.class })
@ComponentScan(basePackages = {
        "springfox.documentation.swagger2.readers.parameter",
        "com.paner.swagger.soa.service",
        "com.paner.swagger.soa.mapper",
        "springfox.documentation.swagger2.mappers"
})
public class Swagger3DocumentationConfiguration {

    @Bean
    public JacksonModuleRegistrar swagger2Module() {
        return new Swagger2JacksonModule();
    }


}
