package com.paner.swagger.soa.service.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.paner.swagger.soa.mapper.ServiceModelToPizzaMapper;
import com.paner.swagger.soa.models.PizzaDocket;
import com.paner.swagger.soa.models.PizzaSwagger;
import com.paner.swagger.soa.models.PizzaSwaggerResponse;
import com.paner.swagger.soa.plugins.PizzaHttpRequest;
import com.paner.swagger.soa.service.ISwagger3Service;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;


/**
 * Created by paner on 17/2/26.
 */
@Service
public class Swagger3ServiceImpl implements ISwagger3Service {


    @Value("${springfox.documentation.swagger.v2.host:DEFAULT}")
    private String hostNameOverride;

    @Autowired
    private DocumentationCache documentationCache;

    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    @Autowired
    private ServiceModelToPizzaMapper pizzaMapper;

    @Autowired
    private JsonSerializer jsonSerializer;

    @Autowired
    private PizzaDocket pizzaDocket;


    public Json getApiDocs(){
        return  getDocumentation().getBody();
    }


    public ResponseEntity<Json> getDocumentation() {
        String swaggerGroup = null;
        String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        ResponseEntity<Json> retJson = null;
        if (documentation == null) {
            return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        swagger.host(hostName());
        return new ResponseEntity<Json>(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    public String getPizzaDoc(String host,String token){
        if (host==null){
            return "invalid argument:host don't allow null.";
        }
        if (token==null){
            return "invalid argument:token don't allow null.";
        }
        String swaggerGroup = null;
        String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            return String.valueOf(HttpStatus.NOT_FOUND);
        }
        PizzaSwagger pizzaSwagger =pizzaMapper.mapPizzaDoc(httpGet(host,token),documentation);

        return httpPost(host,token,pizzaSwagger);
    }

    private PizzaSwagger  httpGet(String host,String token){
        String url=host+"/projects/"+pizzaDocket.getAppid()+"/routeversions/latest";
        String cookie="COFFEE_TOKEN="+token;
        String response = PizzaHttpRequest.get(url, null, cookie);
        PizzaSwaggerResponse pizzaSwaggerResponse =  new Gson().fromJson(response, PizzaSwaggerResponse.class);
        PizzaSwagger originPizzaSwagger = new PizzaSwagger();
        originPizzaSwagger.setRoutes(pizzaSwaggerResponse.getRoutes());
        originPizzaSwagger.setCheck_version_id(originPizzaSwagger.getCheck_version_id());
        return originPizzaSwagger;
    }

    private String httpPost(String host,String token,PizzaSwagger requestSwagger){
        String postUrl = host+"/projects/"+pizzaDocket.getAppid()+"/routeversions";
        String cookie="COFFEE_TOKEN="+token;
        return PizzaHttpRequest.post(postUrl,null,new Gson().toJson(requestSwagger),cookie);
    }

    private String hostName() {
        return hostNameOverride;
    }
}
