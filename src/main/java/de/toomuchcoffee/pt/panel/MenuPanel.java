package de.toomuchcoffee.pt.panel;

import de.toomuchcoffee.pt.page.DashboardPage;
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

        Link<Void> logoutLink = new Link<>("logoutLink") {
            @Override
            public void onClick() {
                getSession().invalidate();
                setResponsePage(DashboardPage.class);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(session.isSignedIn());
            }
        };
        add(logoutLink);
    }
}
