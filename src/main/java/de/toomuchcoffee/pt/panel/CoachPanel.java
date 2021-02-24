package de.toomuchcoffee.pt.panel;


import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class CoachPanel extends Panel {

    public CoachPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("coachHeader", (String) getSession().getAttribute("username")));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole("COACH") || session.getRoles().hasRole("ADMIN"));
    }
}
