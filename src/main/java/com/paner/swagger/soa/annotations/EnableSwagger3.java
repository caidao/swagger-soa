package com.paner.swagger.soa.annotations;


import com.paner.swagger.soa.configuration.Swagger3DocumentationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by www-data on 17/2/23.
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({Swagger3DocumentationConfiguration.class})
public @interface EnableSwagger3 {
}
