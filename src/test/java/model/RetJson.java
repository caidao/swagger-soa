package model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by paner on 17/2/27.
 */
@ApiModel(value = "RetJson",discriminator = "返回值")
public class RetJson {


    public RetJson(String body) {
        this.body = body;
    }

    @ApiModelProperty(value = "数据",dataType = "string")
    private String body;

    @ApiModelProperty(value = "名字",dataType = "string")
    private String name;

    @ApiModelProperty(value = "id",dataType = "int")
    private int id;

    private Param1Model param1Model;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Param1Model getParam1Model() {
        return param1Model;
    }

    public void setParam1Model(Param1Model param1Model) {
        this.param1Model = param1Model;
    }
}
