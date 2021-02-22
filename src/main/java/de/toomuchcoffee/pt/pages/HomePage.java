package de.toomuchcoffee.pt.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@WicketHomePage
@AuthorizeInstantiation("USER")
public class HomePage extends LayoutPage {
}