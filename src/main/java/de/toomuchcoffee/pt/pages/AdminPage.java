package de.toomuchcoffee.pt.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("ADMIN")
public class AdminPage extends LayoutPage {
    public AdminPage(PageParameters parameters) {
        super(parameters);
    }
}