package com.paner.swagger.soa.plugins;


import com.paner.swagger.soa.models.MethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by www-data on 17/2/23.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SoaServiceRequestHandlerProvider implements RequestHandlerProvider {
    private final List<RequestHandler> handlerMappings;
    private final Map<String,MethodModel> methodModelMappings;

    @Autowired
    public SoaServiceRequestHandlerProvider(Defaults defaults) {
        handlerMappings = new ArrayList<RequestHandler>();
        methodModelMappings = new HashMap<>();
    }



    public List<RequestHandler> requestHandlers() {
         return handlerMappings;
    }

    public Map<String,MethodModel> requetMethodMaps(){
        return methodModelMappings;
    }

    public void addRequestHandler(RequestHandler requestHandler){
        handlerMappings.add(requestHandler);
    }

    public void addMethodModelMapping(String url,MethodModel model){
        methodModelMappings.put(url,model);
    }
}
