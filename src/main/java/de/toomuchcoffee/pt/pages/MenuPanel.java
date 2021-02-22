package de.toomuchcoffee.pt.pages;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class MenuPanel extends Panel {
    public MenuPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();

        Link<Void> adminLink = new Link<>("adminLink") {
            @Override
            public void onClick() {
                MenuPanel.this.setResponsePage(AdminPage.class);
            }
        };
        adminLink.setVisible(session.isSignedIn() && session.getRoles().hasRole("ADMIN"));
        add(adminLink);

        Link<Void> logoutLink = new Link<>("logoutLink") {
            @Override
            public void onClick() {
                getSession().invalidate();
            }
        };
        logoutLink.setVisible(session.isSignedIn());
        add(logoutLink);
    }
}
