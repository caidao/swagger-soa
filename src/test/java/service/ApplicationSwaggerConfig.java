package service;


import com.paner.swagger.soa.annotations.EnableSwagger3;
import com.paner.swagger.soa.models.PizzaDocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by www-data on 17/2/23.
 */
@EnableSwagger3
@ComponentScan(basePackages = {"service"})
@Configuration
public class ApplicationSwaggerConfig {
    @Bean
    public Docket addUserDocket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiInfo apiInfo = new ApiInfo(
                "SOA API 文档",
                "API Document管理",
                "V3.8.0",
                "www.paner.com",
                "paner",
                "beta测试环境",
                "https://github.com/caidao/swagger-soa"
        );
        docket.apiInfo(apiInfo).select().
                apis(RequestHandlerSelectors.basePackage("service"))
                .paths(PathSelectors.any()).build();
        return docket;
    }

    @Bean
    public PizzaDocket addSoaPizzaDocket(){
        PizzaDocket pizzaDocket = new PizzaDocket("project-name","stable");
        pizzaDocket.addPlugins("plugin 1-1");
        return pizzaDocket;
    }

}
