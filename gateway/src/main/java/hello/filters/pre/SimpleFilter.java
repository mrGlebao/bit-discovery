package hello.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFilter extends ZuulFilter {

  private static Logger LOGGER = LoggerFactory.getLogger(SimpleFilter.class);

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
    System.out.println("Shit!");
    RequestContext ctx = RequestContext.getCurrentContext();
  //  List<InstanceInfo> instances = eurekaClient.getInstancesById("photo-service");
  //  Map<String, String> metadata = instances.get(0).getMetadata();
    return null;
  }

}
