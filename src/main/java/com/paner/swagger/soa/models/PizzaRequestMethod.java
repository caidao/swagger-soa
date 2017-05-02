package com.paner.swagger.soa.models;

import java.util.List;
import java.util.Map;

/**
 * Created by paner on 17/2/28.
 */
public class PizzaRequestMethod {

    private String  method;

    private String url;

    private List<String> tags;

    private Map<String,Object> params;

    private PizzaFromModel from;

    private List<String> plugins;

    private String comment;

    private Object result;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public PizzaFromModel getFrom() {
        return from;
    }

    public void setFrom(PizzaFromModel from) {
        this.from = from;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
