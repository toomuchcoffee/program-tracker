package de.toomuchcoffee.pt.pages;


import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class ClientDashboardPanel extends Panel {

    public ClientDashboardPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("clientDashboardHeader", (String) getSession().getAttribute("username")));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole("CLIENT") || session.getRoles().hasRole("ADMIN"));
    }
}
