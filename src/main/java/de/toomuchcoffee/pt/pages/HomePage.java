package de.toomuchcoffee.pt.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@WicketHomePage
@AuthorizeInstantiation("USER")
public class HomePage extends LayoutPage {
    public HomePage(PageParameters parameters) {
        super(parameters);
    }
}