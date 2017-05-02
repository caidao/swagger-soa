package service;

import com.paner.swagger.soa.service.ISwagger3Service;
import iface.ISwaggerTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.Car;
import model.RetJson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by www-data on 17/2/23.
 */
@Service
@RequestMapping(value = "/platform",produces="application/json;charset=utf-8")
@Api(value = "BuTargetBoardController",description = "平台-指标看板")
public class SwaggerTestImpl implements ISwaggerTest {



    @RequestMapping(value = "/stats/search",method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET",value = "你好世界")
    public String helloWorld(String info) {
        return "Hello world for Swagger,"+info;
    }


    @RequestMapping(value = "/stats/anny",method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST",value = "hello-anny")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "info",value = "信息",required = false,paramType = "query", dataType = "string",defaultValue = "10778749"),
            @ApiImplicitParam(name = "day",value = "天数",required = false,paramType = "query", dataType = "int",defaultValue = "10778749")
    })
    public String hellAnny(String info,int day) {
        return "Hello anny for Swagger,"+info;
    }

    @RequestMapping(value = "/stats/anny2",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "info",value = "信息",required = false,paramType = "query", dataType = "string",defaultValue = "10778749")
    })
    public Object hellAnny2(String info, RetJson param) {
        return new RetJson("Hello anny for Swagger,"+param.getBody());
    }

    @RequestMapping(value = "/stats/anny2",method = RequestMethod.DELETE,produces = "timeout:30000")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "info",value = "信息",required = false,paramType = "query", dataType = "string",defaultValue = "10778749")
    })
    public Object hellAnny3(String info,RetJson param) {
        return new RetJson("Hello anny for Swagger,"+param.getBody());
    }

    @RequestMapping(value = "/stats/anny2",method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "info",value = "信息",required = false,paramType = "query", dataType = "string",defaultValue = "10778749")
    })
    public Object hellAnny4(String info,@RequestBody Car car) {
        return new RetJson("Hello anny for Swagger,"+car.getMake());
    }

    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        ISwaggerTest test = context.getBean(ISwaggerTest.class);
        System.out.println(test.helloWorld("it is good day!"));

        ISwagger3Service swagger3Service = context.getBean(ISwagger3Service.class);
        System.out.println(swagger3Service.getApiDocs().value());
        //System.out.println(swagger3Service.getDocumentation().getBody().value());

    }




}
