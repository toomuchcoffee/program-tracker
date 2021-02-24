package de.toomuchcoffee.pt.page;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import de.toomuchcoffee.pt.panel.ClientPanel;
import de.toomuchcoffee.pt.panel.CoachPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@WicketHomePage
@AuthorizeInstantiation("USER")
public class DashboardPage extends LayoutPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new ClientPanel("clientPanel"));
        add(new CoachPanel("coachPanel"));
    }
}