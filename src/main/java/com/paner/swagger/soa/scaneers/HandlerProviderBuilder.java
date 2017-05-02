package com.paner.swagger.soa.scaneers;


import com.paner.swagger.soa.models.MethodModel;
import com.paner.swagger.soa.models.PizzaDocket;
import com.paner.swagger.soa.models.PizzaFromModel;
import com.paner.swagger.soa.plugins.SoaServiceRequestHandlerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by paner on 17/2/26.
 */
public class HandlerProviderBuilder {

    private SoaServiceRequestHandlerProvider handlerProvider =null ;
    private static final Logger log = LoggerFactory.getLogger(HandlerProviderBuilder.class);
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
    private  ConfigurableListableBeanFactory beanFactory;
    private PizzaDocket pizzaDocket;

    public HandlerProviderBuilder(SoaServiceRequestHandlerProvider handlerProvider, PizzaDocket pizzaDocket){
        this.handlerProvider = handlerProvider;
        this.pizzaDocket = pizzaDocket;
    }

    public void builder(ConfigurableListableBeanFactory configurableListableBeanFactory){
        beanFactory = configurableListableBeanFactory;
        configurableListableBeanFactory.getBeanDefinitionNames();
        for (String beanName:configurableListableBeanFactory.getBeanDefinitionNames()){
            parse(configurableListableBeanFactory.getBeanDefinition(beanName).getBeanClassName(),beanName);
        }

    }


    private void parse(String className,String beanName){
        try {
            if (className==null){
                return;
            }
            MetadataReader meta = metadataReaderFactory.getMetadataReader(className);
            if (meta.getAnnotationMetadata().hasAnnotation("org.springframework.stereotype.Service") &&
                    meta.getAnnotationMetadata().hasAnnotation("io.swagger.annotations.Api")){


                AnnotationMetadata an =  meta.getAnnotationMetadata();
                //获取类方法上的RequestMapping的注解信息
                Set<MethodMetadata> metadataReaderSet = an.getAnnotatedMethods("org.springframework.web.bind.annotation.RequestMapping");

                //获取类上的RequestMapping的注解信息
                AnnotationMetadataReadingVisitor cd = (AnnotationMetadataReadingVisitor) meta.getClassMetadata();
                MultiValueMap<String, Object> requestMapAttr =  cd.getAllAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping");

                buildHandlerMethod(beanName,className,((String[])requestMapAttr.get("value").get(0))[0],methodRequestMappings(metadataReaderSet));
            }
        }catch (FileNotFoundException ex){

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
            if (methodMetadata.getAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping")!=null){
                methodAnnotations.put(methodMetadata.getMethodName(),
                        methodMetadata.getAllAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping",true));
            }
        }
        return methodAnnotations;
    }

    /**
     * 解析生成requestMappingInfo对象
     * @param urls
     * @param requestMapAttr
     * @return
     */
    private RequestMappingInfo buildRequestMappingInfo(String[] urls, MultiValueMap<String, Object>requestMapAttr){
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
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class bean = Class.forName(className);
        Object beanObj = bean.newInstance();
        Method[] methods = bean.getDeclaredMethods();
        for (Method method:methods){
            if (methodAnnotations.containsKey(method.getName())) {
                String[] urls = getUrls(classUrlPath != null ? classUrlPath : "", methodAnnotations.get(method.getName()));
                MultiValueMap<String, Object>requestMapAttr=methodAnnotations.get(method.getName());
                handlerProvider.addRequestHandler(
                        new RequestHandler(buildRequestMappingInfo(urls, requestMapAttr),
                                new HandlerMethod(beanObj, method))
                );
                buildMethodModel(urls[0]+getRequestMethod(methodAnnotations.get(method.getName())),method,bean.getInterfaces(),requestMapAttr.get("produces"));
            }
        }
    }

    private String getRequestMethod(MultiValueMap<String, Object>requestMapAttr){
        String method;
        try{
            RequestMethod[] requestMethod = (RequestMethod[]) requestMapAttr.get("method").get(0);
            method = requestMethod[0].name();
        }catch (Exception ex){
            method="";
        }

        return method;
    }

    private String[] getUrls(String classUrlPath,MultiValueMap<String, Object>requestMapAttr){
        String[] urls = (String[]) requestMapAttr.get("value").get(0);
        for (int i=0;i<urls.length;i++){
            urls[i] = classUrlPath+urls[i];
        }
        return urls;
    }

    private void buildMethodModel(String url,Method method,Class[] interfaces,Object params){
        MethodModel methodModel = new MethodModel();
        PizzaFromModel fromModel = new PizzaFromModel();
        try {
            if (params instanceof List){
                List<String[]> paramList = (List<String[]>) params;
                for (String[] temps:paramList){
                    if (temps.length==0) continue;
                    String temp = temps[0];
                    if (temp!=null && temp.contains("timeout")){
                        fromModel.setTimeout(Long.valueOf(temp.split(":")[1]));
                    }
                }
            }
        }catch (Exception ex){
            log.info(ex.getMessage(),ex);
        }
        fromModel.setAppid(pizzaDocket.getAppid());
        fromModel.setClusterName(pizzaDocket.getClusterName());
        fromModel.setMethodName(method.getName());
        if (interfaces.length>0){
            fromModel.setIface(interfaces[0].getName());
        }

        methodModel.setFromModel(fromModel);
        methodModel.setPlugins(pizzaDocket.getPlugins());
        handlerProvider.addMethodModelMapping(url,methodModel);


    }
}
