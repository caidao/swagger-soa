package com.paner.swagger.soa.configuration;

import springfox.documentation.spring.web.paths.AbstractPathProvider;

/**
 * Created by www-data on 17/2/23.
 */
public class SoaRelativePathProvider extends AbstractPathProvider {

    public static final String ROOT = "/";

    @Override
    protected String applicationPath() {
        return ROOT;
    }

    @Override
    protected String getDocumentationPath() {
        return ROOT;
    }
}
