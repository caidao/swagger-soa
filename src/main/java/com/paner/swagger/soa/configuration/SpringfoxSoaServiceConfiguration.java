package com.paner.swagger.soa.configuration;

import org.springframework.context.annotation.*;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import springfox.documentation.schema.configuration.ModelsConfiguration;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.service.*;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.ObjectMapperConfigurer;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.util.List;

/**
 * Created by www-data on 17/2/23.
 */


@Configuration
@Import({ ModelsConfiguration.class })
@ComponentScan(basePackages = {
        "springfox.documentation.spring.web.scanners",
        "springfox.documentation.spring.web.readers.operation",
        "springfox.documentation.spring.web.readers.parameter",
        "com.paner.swagger.soa.plugins",
        "springfox.documentation.spring.web.plugins",
        "springfox.documentation.spring.web.paths",
        "springfox.documentation.spring.web.caching"
},excludeFilters ={@ComponentScan.Filter(value = DocumentationPluginsBootstrapper.class,type=FilterType.ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = WebMvcRequestHandlerProvider.class,type=FilterType.ASSIGNABLE_TYPE)})
@EnablePluginRegistries({ DocumentationPlugin.class,
        ApiListingBuilderPlugin.class,
        OperationBuilderPlugin.class,
        ParameterBuilderPlugin.class,
        ExpandedParameterBuilderPlugin.class,
        ResourceGroupingStrategy.class,
        OperationModelsProviderPlugin.class,
        DefaultsProviderPlugin.class,
        PathDecorator.class
})
@EnableAspectJAutoProxy
public class SpringfoxSoaServiceConfiguration {

    @Bean
    public Defaults defaults() {
        return new Defaults();
    }

    @Bean
    public DocumentationCache resourceGroupCache() {
        return new DocumentationCache();
    }

    @Bean
    public static ObjectMapperConfigurer objectMapperConfigurer() {
        return new ObjectMapperConfigurer();
    }

    @Bean
    public JsonSerializer jsonSerializer(List<JacksonModuleRegistrar> moduleRegistrars) {
        return new JsonSerializer(moduleRegistrars);
    }

}
