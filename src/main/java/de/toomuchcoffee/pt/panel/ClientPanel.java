package de.toomuchcoffee.pt.panel;


import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import static de.toomuchcoffee.pt.domain.entity.Role.CLIENT;

public class ClientPanel extends Panel {

    public ClientPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("clientHeader", String.format("Client Dashboard: %s", getSession().getAttribute("username"))));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole(CLIENT.name()));
    }
}
