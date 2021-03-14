package de.toomuchcoffee.pt.panel;


import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import static de.toomuchcoffee.pt.domain.entity.Role.COACH;

public class CoachPanel extends Panel {

    public CoachPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("coachHeader", String.format("Coach Dashboard: %s", getSession().getAttribute("username"))));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole(COACH.name()));
    }
}
