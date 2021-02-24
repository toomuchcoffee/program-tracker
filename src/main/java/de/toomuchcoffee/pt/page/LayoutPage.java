package de.toomuchcoffee.pt.page;

import de.toomuchcoffee.pt.panel.MenuPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;

public class LayoutPage extends WebPage {
    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new MenuPanel("menuPanel"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        PackageResourceReference cssFile =
                new PackageResourceReference(this.getClass(), "css/styles.css");
        CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);

        response.render(cssItem);
    }
}
