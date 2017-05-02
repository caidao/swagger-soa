package com.paner.swagger.soa.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by paner on 17/3/3.
 */
public class PizzaDocket {

    private String appid;

    private String clusterName;

    private List<String> plugins;

    public PizzaDocket(String appid, String clusterName) {
        this.appid = appid;
        this.clusterName = clusterName;
        plugins = new ArrayList<>();
    }



    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void addPlugins(String plugin) {
        this.plugins.add(plugin);
    }
}
