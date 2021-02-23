package de.toomuchcoffee.pt.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@WicketHomePage
@AuthorizeInstantiation("USER")
public class DashboardPage extends LayoutPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new ClientDashboardPanel("clientDashboard"));
        add(new CoachDashboardPanel("coachDashboard"));
    }
}