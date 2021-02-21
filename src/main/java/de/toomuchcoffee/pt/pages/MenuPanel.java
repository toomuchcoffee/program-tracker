package de.toomuchcoffee.pt.pages;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class MenuPanel extends Panel {
    public MenuPanel(String id) {
        super(id);
        add(new Link<Void>("adminLink") {
            @Override
            public void onClick() {
                MenuPanel.this.setResponsePage(AdminPage.class);
            }
        });
    }
}
