package com.paner.swagger.soa.configuration;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spring.web.plugins.DefaultConfiguration;

/**
 * Created by www-data on 17/2/23.
 */
public class SoaDefaultConfiguration extends DefaultConfiguration {

    private final Defaults defaults;
    private final TypeResolver typeResolver;

    public SoaDefaultConfiguration(Defaults defaults,
                                TypeResolver typeResolver) {
        super(defaults,typeResolver,null);
        this.defaults = defaults;
        this.typeResolver = typeResolver;
    }


    public DocumentationContextBuilder create(DocumentationType documentationType) {
        return new DocumentationContextBuilder(documentationType)
                .operationOrdering(defaults.operationOrdering())
                .apiDescriptionOrdering(defaults.apiDescriptionOrdering())
                .apiListingReferenceOrdering(defaults.apiListingReferenceOrdering())
                .additionalIgnorableTypes(defaults.defaultIgnorableParameterTypes())
                .rules(defaults.defaultRules(typeResolver))
                .defaultResponseMessages(defaults.defaultResponseMessages())
                .pathProvider(new SoaRelativePathProvider())
                .typeResolver(typeResolver)
                .enableUrlTemplating(false)
                .selector(ApiSelector.DEFAULT);
    }


    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
