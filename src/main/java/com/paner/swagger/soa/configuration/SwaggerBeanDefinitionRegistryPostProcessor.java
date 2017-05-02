package com.paner.swagger.soa.configuration;

import com.paner.swagger.soa.plugins.SoaServiceRequestHandlerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by paner on 17/2/24.
 */
public class SwaggerBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private SoaServiceRequestHandlerProvider handlerProvider =null ;
    private static final Logger log = LoggerFactory.getLogger(SwaggerBeanDefinitionRegistryPostProcessor.class);
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    public SwaggerBeanDefinitionRegistryPostProcessor(SoaServiceRequestHandlerProvider handlerProvider){
        this.handlerProvider = handlerProvider;
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("postProcessBeanDefinitionRegistry ... ");

        String[] candidateNames = registry.getBeanDefinitionNames();
        for (String beanName : candidateNames){
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            parse(beanDef.getBeanClassName(),beanName);

        }
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("postProcessBeanFactory ... ");
    }


    private void parse(String className,String beanName){
        try {
            MetadataReader meta = metadataReaderFactory.getMetadataReader(className);
            if (meta.getAnnotationMetadata().hasAnnotation("org.springframework.stereotype.Controller") &&
                    meta.getAnnotationMetadata().hasAnnotation("io.swagger.annotations.Api")){


                AnnotationMetadata an =  meta.getAnnotationMetadata();
                //获取类方法上的RequestMapping的注解信息
                Set<MethodMetadata> metadataReaderSet = an.getAnnotatedMethods("org.springframework.web.bind.annotation.RequestMapping");

                //获取类上的RequestMapping的注解信息
                AnnotationMetadataReadingVisitor cd = (AnnotationMetadataReadingVisitor) meta.getClassMetadata();
                MultiValueMap<String, Object>requestMapAttr =  cd.getAllAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping");

                buildHandlerMethod(beanName,className,((String[])requestMapAttr.get("value").get(0))[0],methodRequestMappings(metadataReaderSet));
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
        }
    }


    /**
     * 建立method名字与RequestMapping的映射关系
     * @param metadataReaderSet
     * @return
     */
    private Map<String,MultiValueMap<String,Object>> methodRequestMappings(Set<MethodMetadata> metadataReaderSet){
        Map<String,MultiValueMap<String,Object>> methodAnnotations = new HashMap<String, MultiValueMap<String,Object>>();
        Iterator it = metadataReaderSet.iterator();
        while (it.hasNext()){
             MethodMetadataReadingVisitor methodMetadata = (MethodMetadataReadingVisitor) it.next();
             methodAnnotations.put(methodMetadata.getMethodName(),
                     methodMetadata.getAllAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping",true));
        }
        return methodAnnotations;
    }

    /**
     * 解析生成requestMappingInfo对象
     * @param classUrlPath
     * @param requestMapAttr
     * @return
     */
    private RequestMappingInfo  buildRequestMappingInfo(String classUrlPath, MultiValueMap<String, Object>requestMapAttr){

        String[] urls = (String[]) requestMapAttr.get("value").get(0);
        for (int i=0;i<urls.length;i++){
            urls[i] = classUrlPath+urls[i];
        }
        RequestMappingInfo mappingInfo = new RequestMappingInfo(requestMapAttr.get("name").toString(),
                new PatternsRequestCondition(urls),
                new RequestMethodsRequestCondition((RequestMethod[]) requestMapAttr.get("method").get(0)),
                new ParamsRequestCondition((String[]) requestMapAttr.get("params").get(0)),
                new HeadersRequestCondition((String[]) requestMapAttr.get("headers").get(0)),
                new ConsumesRequestCondition("application/json"),
                new ProducesRequestCondition("application/json"),
                null);
        return mappingInfo;
    }

    /**
     * 够将handlerMap
     * @param beanName
     * @param className
     * @param classUrlPath
     * @param methodAnnotations
     * @throws ClassNotFoundException
     */
    private void  buildHandlerMethod(String beanName,String className,
                                     String classUrlPath,Map<String,MultiValueMap<String,Object>>methodAnnotations)
            throws ClassNotFoundException {
        Class bean = Class.forName(className);
        Method[] methods = bean.getDeclaredMethods();
        for (Method method:methods){
            if (methodAnnotations.containsKey(method.getName())) {
                handlerProvider.addRequestHandler(
                        new RequestHandler(buildRequestMappingInfo(classUrlPath!=null?classUrlPath:"", methodAnnotations.get(method.getName())),
                        new HandlerMethod(beanName, method))
                );
            }
        }
    }

}
