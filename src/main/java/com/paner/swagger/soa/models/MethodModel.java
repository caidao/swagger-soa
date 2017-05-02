package com.paner.swagger.soa.models;

import java.util.List;

/**
 * Created by paner on 17/3/2.
 */
public class MethodModel {

    private PizzaFromModel fromModel;

    private List<String> plugins;

    public PizzaFromModel getFromModel() {
        return fromModel;
    }

    public void setFromModel(PizzaFromModel fromModel) {
        this.fromModel = fromModel;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }
}
