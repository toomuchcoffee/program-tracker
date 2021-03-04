package de.toomuchcoffee.pt.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

public class MenuPanel extends Panel {
    private Navbar navbar;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        navbar = new Navbar("navbar");
        navbar.setBrandName(Model.of("Program Tracker"));
        add(navbar);
    }
    public MenuPanel(String id) {
        super(id);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (((AbstractAuthenticatedWebSession) getSession()).isSignedIn()) {
            String logoutUrl = ((WebApplication) getApplication()).getServletContext().getContextPath() + "/logout";
            NavbarExternalLink logoutLink = new NavbarExternalLink(Model.of(logoutUrl));
            logoutLink.setLabel(Model.of("logout"));
            navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, logoutLink));
        }
    }
}
