package com.paner.swagger.soa.models;

import java.util.List;
import java.util.Map;

/**
 * Created by paner on 17/3/3.
 */
public class PizzaSwaggerResponse {

    private List<PizzaRequestMethod> routes;

    private int id;

    private String project_name;

    private String hash;

    private Map<String,Object> created_by;

    private  String created_at;

    public List<PizzaRequestMethod> getRoutes() {
        return routes;
    }

    public void setRoutes(List<PizzaRequestMethod> routes) {
        this.routes = routes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Map<String, Object> getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Map<String, Object> created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
