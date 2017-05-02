package com.paner.swagger.soa.service;

import org.springframework.http.ResponseEntity;
import springfox.documentation.spring.web.json.Json;

/**
 * Created by paner on 17/2/26.
 */
public interface ISwagger3Service {

    public Json getApiDocs();

    public ResponseEntity<Json> getDocumentation();

    public String getPizzaDoc(String host, String token);
}
