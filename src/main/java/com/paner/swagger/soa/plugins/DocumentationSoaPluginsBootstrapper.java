package com.paner.swagger.soa.plugins;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paner.swagger.soa.configuration.SoaDefaultConfiguration;
import com.paner.swagger.soa.models.PizzaDocket;
import com.paner.swagger.soa.scaneers.HandlerProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static springfox.documentation.spi.service.contexts.Orderings.pluginOrdering;

/**
 * Created by www-data on 17/2/23.
 */
@Component
public class DocumentationSoaPluginsBootstrapper implements ApplicationListener<ContextRefreshedEvent>,ApplicationContextAware,DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(DocumentationSoaPluginsBootstrapper.class);
    private final DocumentationPluginsManager documentationPluginsManager;
    private final SoaServiceRequestHandlerProvider handlerProvider;
    private final DocumentationCache scanned;
    private final ApiDocumentationScanner resourceListing;
    private final SoaDefaultConfiguration defaultConfiguration;
    private static ApplicationContext applicationContext = null;
    private final PizzaDocket pizzaDocket;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    public DocumentationSoaPluginsBootstrapper(DocumentationPluginsManager documentationPluginsManager,
                                               SoaServiceRequestHandlerProvider handlerProvider,
                                               DocumentationCache scanned,
                                               ApiDocumentationScanner resourceListing,
                                               TypeResolver typeResolver,
                                               Defaults defaults, PizzaDocket pizzaDocket) {

        this.documentationPluginsManager = documentationPluginsManager;
        this.handlerProvider = handlerProvider;
        this.scanned = scanned;
        this.resourceListing = resourceListing;
        this.defaultConfiguration = new SoaDefaultConfiguration(defaults, typeResolver);
        this.pizzaDocket = pizzaDocket;
    }


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (initialized.compareAndSet(false, true)) {
            buildHandlerMappings(DocumentationSoaPluginsBootstrapper.applicationContext);
            log.info("Context refreshed");
            List<DocumentationPlugin> plugins = pluginOrdering()
                    .sortedCopy(documentationPluginsManager.documentationPlugins());
            log.info("Found {0} custom documentation plugin(s)", plugins.size());
            for (DocumentationPlugin each : plugins) {
                DocumentationType documentationType = each.getDocumentationType();
                if (each.isEnabled()) {
                    scanDocumentation(buildContext(each));
                } else {
                    log.info("Skipping initializing disabled plugin bean {} v{}",
                            documentationType.getName(), documentationType.getVersion());
                }
            }
        }
    }

    private void buildHandlerMappings(ApplicationContext context){

        if (context instanceof ConfigurableApplicationContext) {
            log.info("convert to ConfigurableApplicationContext...");
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            new HandlerProviderBuilder(handlerProvider,pizzaDocket).builder(configurableApplicationContext.getBeanFactory());
        }
        context.publishEvent(new ObjectMapperConfigured(context,new ObjectMapper()));

    }

    private DocumentationContext buildContext(DocumentationPlugin each) {
        return each.configure(defaultContextBuilder(each));
    }

    private void scanDocumentation(DocumentationContext context) {
        scanned.addDocumentation(resourceListing.scan(context));
    }

    private DocumentationContextBuilder defaultContextBuilder(DocumentationPlugin each) {
        DocumentationType documentationType = each.getDocumentationType();
        return documentationPluginsManager
                .createContextBuilder(documentationType, defaultConfiguration)
                .requestHandlers(handlerProvider.requestHandlers());
    }

    public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);
        if (DocumentationSoaPluginsBootstrapper.applicationContext==null){
            DocumentationSoaPluginsBootstrapper.applicationContext=applicationContext;
        }
    }

    public static void clearHolder() {
        log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }


    public void destroy() throws Exception {
        DocumentationSoaPluginsBootstrapper.clearHolder();
    }
}
