package de.toomuchcoffee.pt.pages;


import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class CoachDashboardPanel extends Panel {

    public CoachDashboardPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("coachDashboardHeader", (String) getSession().getAttribute("username")));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole("COACH") || session.getRoles().hasRole("ADMIN"));
    }
}
