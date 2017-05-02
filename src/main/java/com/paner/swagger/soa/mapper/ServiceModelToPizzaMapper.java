package com.paner.swagger.soa.mapper;

import com.fasterxml.classmate.types.ResolvedObjectType;
import com.google.common.collect.Multimap;
import com.paner.swagger.soa.models.PizzaRequestMethod;
import com.paner.swagger.soa.models.PizzaSwagger;
import com.paner.swagger.soa.plugins.SoaServiceRequestHandlerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paner on 17/3/1.
 */

@Component
public  class ServiceModelToPizzaMapper {


    private final SoaServiceRequestHandlerProvider handlerProvider;

    @Autowired
    public ServiceModelToPizzaMapper(SoaServiceRequestHandlerProvider handlerProvider){
        this.handlerProvider = handlerProvider;
    }

    public PizzaSwagger mapPizzaDoc(PizzaSwagger srcPizzaSwagger,Documentation from){
        PizzaSwagger pizzaSwagger = new PizzaSwagger();
        pizzaSwagger.setRoutes(mapApiListings(from.getApiListings()));
        retainUnchange(srcPizzaSwagger,pizzaSwagger);
        return pizzaSwagger;
    }

    /**
     * 查找未修改的url文档
     * @param srcPizzaSwagger
     * @param descPizzaSwagger
     */
    private void retainUnchange(PizzaSwagger srcPizzaSwagger,PizzaSwagger descPizzaSwagger){
        if (srcPizzaSwagger==null
                || srcPizzaSwagger.getRoutes()==null
                || srcPizzaSwagger.getRoutes().size()==0){
            return;
        }

        boolean bExist;
        for (PizzaRequestMethod requestMethod:srcPizzaSwagger.getRoutes()){
             bExist =false;
            for (PizzaRequestMethod scanRequestMethod:descPizzaSwagger.getRoutes()){
                if (scanRequestMethod.getUrl().equalsIgnoreCase(requestMethod.getUrl())){
                    bExist = true;
                     break;
                }
            }
            //原来的url列表中存在，扫描的url列表不存在的，则添加到扫描列表中
            if (!bExist)
                descPizzaSwagger.getRoutes().add(requestMethod);
        }
        descPizzaSwagger.setCheck_version_id(srcPizzaSwagger.getCheck_version_id());

    }

    /**
     * 获取最新的url文档
     * @param apiListings
     * @return
     */
    private  List<PizzaRequestMethod> mapApiListings(Multimap<String, ApiListing> apiListings){
        Map<String,Model> allModels = mergeModels(apiListings);
        List<PizzaRequestMethod> requestMethods = new ArrayList<>();
        for (ApiListing each : apiListings.values()){
            List<String> tags = new ArrayList<>();
            tags.add(each.getDescription());
            for(ApiDescription desc:each.getApis()){
                PizzaRequestMethod method = new PizzaRequestMethod();
                method.setComment(desc.getOperations().get(0).getSummary());
                method.setUrl(desc.getPath());
                method.setTags(tags);
                method.setParams(mapParameter(desc.getOperations(), allModels));
                String methodName =desc.getOperations().get(0).getMethod().name();
                method.setMethod(methodName);
                method.setFrom(handlerProvider.requetMethodMaps().get(desc.getPath()+methodName).getFromModel());
                method.setPlugins(handlerProvider.requetMethodMaps().get(desc.getPath()+methodName).getPlugins());
                requestMethods.add(method);
            }

        }
        return requestMethods;
    }

    private Map<String,Model> mergeModels(Multimap<String, ApiListing> apiListings){
        Map<String,Model> allModels = new HashMap<>();
        for (ApiListing each : apiListings.values()){
            allModels.putAll(each.getModels());
        }
        return allModels;
    }


    private   Map<String,Object> mapParameter(List<Operation> operations,Map<String,Model>models){
        Map<String,Object>map = new HashMap<>();
        if (operations==null || operations.size()==0){
            return map;
        }
        List<Parameter> parameters =operations.get(0).getParameters();
        Object param ;
        for (Parameter parameter:parameters){
            try {
                String  className = ((ResolvedObjectType)parameter.getType().get()).getErasedType().getName();
                if("body".equalsIgnoreCase(parameter.getParamType()) &&
                        (param= bodyParam(models,className(className)))!=null){
                    map.put(parameter.getName(),param);
                }else {
                    map.put(inputKey(parameter.getName()),inputValue(parameter.getName(),parameter.getDescription()));
                }
            }catch (Exception ex){
                map.put(inputKey(parameter.getName()),inputValue(parameter.getName(),parameter.getDescription()));
            }


        }
        return map;
    }

    private  String inputKey(String name){
        return "@"+name;
    }

    private  String inputValue(String value,String comment){
        String temp="input:$."+value;
        //return comment!=null&&!comment.equalsIgnoreCase(value)?temp+" #"+comment:temp;
        return temp;
    }

    private  Map<String,Object> bodyParam(Map<String,Model>allModelMap,String modelName){
        Model model = allModelMap.get(modelName);
        if (model==null){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<String,ModelProperty> entry:model.getProperties().entrySet()){
            String className = className(entry.getValue().getQualifiedType());
            if (allModelMap.containsKey(className)){
                map.put(inputKey(entry.getKey()),bodyParam(allModelMap,className));
            }else
                map.put(inputKey(entry.getKey()),inputValue(entry.getKey(),entry.getValue().getDescription()));
        }

        return map;
    }

    private  String className(String className){
        String names[] = className.split("\\.");
        return names[names.length-1];
    }


}
