package de.toomuchcoffee.pt.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

public class MenuPanel extends Panel {
    private Navbar navbar;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        navbar = new Navbar("navbar");
        navbar.setBrandName(Model.of("Program Tracker"));

        String logoutUrl = ((WebApplication) getApplication()).getServletContext().getContextPath() + "/logout";
        LogoutLink logoutLink = new LogoutLink(Model.of(logoutUrl));
        logoutLink.setLabel(Model.of("logout"));
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, logoutLink));

        add(navbar);
    }
    public MenuPanel(String id) {
        super(id);
    }

    public static class LogoutLink extends NavbarExternalLink {
        public LogoutLink(IModel<String> href) {
            super(href);
        }

        @Override
        protected void onConfigure() {
            super.onConfigure();
            setVisible(((AbstractAuthenticatedWebSession) getSession()).isSignedIn());
        }
    }
}
