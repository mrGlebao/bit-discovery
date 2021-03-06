package ru.sbt.ds.filters.route;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;

public class VersionFilter extends ZuulFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(VersionFilter.class);

    private EurekaClient eurekaClient;

    public VersionFilter(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            ctx.setRouteHost(new URL(getAppropriateRoutingUrl(ctx) + formQuery(ctx)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAppropriateRoutingUrl(RequestContext context) {
        List<Application> instances = eurekaClient.getApplications().getRegisteredApplications();//getInstancesById("photo-service");

        List<InstanceInfo> photoServices = null;
        for (Application app : instances) {
            if ("photo-service".equals(app.getName().toLowerCase())) {
                photoServices = app.getInstances();
                break;
            }
        }

        //Choose right url
        if(photoServices == null || photoServices.isEmpty()) {
            // There may be some failover logic
            return EMPTY_STRING;
        }
        InstanceInfo maxVersionPhotoService = photoServices.get(0);
        int version = Integer.valueOf(maxVersionPhotoService.getMetadata().get("version"));
        for(InstanceInfo photoService : photoServices){
            int newVersion = Integer.valueOf(photoService.getMetadata().get("version"));
            if(newVersion > version) {
                maxVersionPhotoService = photoService;
                version = Integer.valueOf(maxVersionPhotoService.getMetadata().get("version"));
            }
        }
        return maxVersionPhotoService.getHomePageUrl();
    }

    private String formQuery(RequestContext context) {
        if (context == null || context.getRequest() == null) {
            return EMPTY_STRING;
        }
        return context.getRequest().getRequestURI() + "?" + context.getRequest().getQueryString();
    }

}

