package com.paner.swagger.soa.plugins;

import com.google.gson.Gson;
import com.paner.swagger.soa.models.PizzaSwagger;
import com.paner.swagger.soa.models.PizzaSwaggerResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * Created by paner on 17/3/3.
 */
public class PizzaHttpRequest {
    private static CloseableHttpClient httpClient = null;
    private static final Logger LOGGER= LoggerFactory.getLogger(PizzaHttpRequest.class);

    static {
        try {
//            SSLContext sslContext = new SSLContextBuilder()
//                    .loadTrustMaterial(null, new TrustStrategy() {
//                        @Override
//                        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//                            return true;
//                        }
//
//                    }).build();
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                    sslContext);
//            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("https", sslsf)
//                    .build();
//
//            HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
//            httpClient = HttpClients.custom()
//                    .setConnectionManager(cm)
//                    .build();
            httpClient = HttpClients.createDefault();
        } catch (Exception e) {
            LOGGER.info("https create failure.", e);
            httpClient = HttpClients.createDefault();
        }
    }



    public static   String get(String url,List<NameValuePair> params,String cookie){

        LOGGER.info("latest url:"+url);
        String body = null;
        try {
            // Get请求
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Cookie",cookie);

            // 设置参数
            if (params==null)
            {
                httpget.setURI(new URI(httpget.getURI().toString()));
            }else {
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
                httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
            }

            // 发送请求
            HttpResponse httpresponse = httpClient.execute(httpget);
            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity);
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return body;
    }


    public static   String post(String url,List<NameValuePair> params,String content,String cookie) {

        String body = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Cookie",cookie);
            httpPost.setHeader("content-type", "application/json");
            if (params!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }else {
                httpPost.setEntity(new StringEntity(content, "UTF-8"));
            }

            HttpResponse response = httpClient.execute(httpPost);
            // 获取返回数据
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity);
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return body;
    }

    public static void  main(String args[]){
        PizzaHttpRequest request = new PizzaHttpRequest();
        String url="http://***";
        String cookie="COFFEE_TOKEN=f8ab62ac-6819-43f9-b252-b98607c7db6eTb8deec59-7c4d-48b8-8ffc-c54b26b8515dT";
        String response = request.get(url, null, cookie);
        PizzaSwaggerResponse pizzaSwaggerResponse =  new Gson().fromJson(response, PizzaSwaggerResponse.class);
        System.out.println(response);

        PizzaSwagger swagger = new PizzaSwagger();
        swagger.setRoutes(pizzaSwaggerResponse.getRoutes());
        swagger.setCheck_version_id(pizzaSwaggerResponse.getId());
        swagger.getRoutes().get(0).setMethod("testst");
        String postUrl = "http://***";
        System.out.println(request.post(postUrl, null, new Gson().toJson(swagger), cookie));
    }


}
