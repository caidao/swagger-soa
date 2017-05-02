package com.paner.swagger.soa.models;

import java.util.List;

/**
 * Created by paner on 17/2/28.
 */
public class PizzaSwagger {

    private List<PizzaRequestMethod> routes;

    private int check_version_id;

    public List<PizzaRequestMethod> getRoutes() {
        return routes;
    }

    public void setRoutes(List<PizzaRequestMethod> routes) {
        this.routes = routes;
    }

    public int getCheck_version_id() {
        return check_version_id;
    }

    public void setCheck_version_id(int check_version_id) {
        this.check_version_id = check_version_id;
    }
}
