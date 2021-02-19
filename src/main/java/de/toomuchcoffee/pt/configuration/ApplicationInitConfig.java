package de.toomuchcoffee.pt.configuration;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class ApplicationInitConfig implements WicketApplicationInitConfiguration {

  @Override
  public void init(WebApplication webApplication) {
    webApplication.getCspSettings().blocking().disabled();
  }

}