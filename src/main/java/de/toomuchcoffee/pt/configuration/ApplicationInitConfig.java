package de.toomuchcoffee.pt.configuration;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class ApplicationInitConfig implements WicketApplicationInitConfiguration {

  @Override
  public void init(WebApplication webApplication) {
    webApplication.getCspSettings().blocking().disabled();

    final BootstrapSettings settings = new BootstrapSettings();
    settings.useCdnResources(true);
    final ThemeProvider themeProvider = new BootswatchThemeProvider(BootswatchTheme.Spacelab);
    settings.setThemeProvider(themeProvider);

    Bootstrap.install(webApplication, settings);
  }

}