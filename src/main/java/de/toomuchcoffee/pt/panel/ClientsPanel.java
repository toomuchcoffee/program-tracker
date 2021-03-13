package de.toomuchcoffee.pt.panel;

import de.toomuchcoffee.pt.dto.UpdateUserDto;
import de.toomuchcoffee.pt.service.UserService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static de.toomuchcoffee.pt.domain.entity.Role.COACH;

public class ClientsPanel extends Panel {
    @SpringBean
    private UserService userService;

    private String selectedClient;

    private final UpdateUserDto user;

    public ClientsPanel(String id, UpdateUserDto user) {
        super(id);
        this.user = user;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        List<String> clients = userService.findAvailableClients();
        DropDownChoice<String> dropDownChoice = new DropDownChoice<>("availableClients", PropertyModel.of(this, "selectedClient"), clients);
        dropDownChoice.add(new FormComponentUpdatingBehavior() {
            protected void onUpdate() {
                userService.addClient(user, dropDownChoice.getModel().getObject());
            }
        });
        add(dropDownChoice);

        DataView<String> dataView = new ClientListDataView("clientList", new ListDataProvider<>() {
            @Override
            protected List<String> getData() {
                return userService.findClientsForCoach(user.getUsername());
            }
        });

        add(dataView);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(user.getRole() == COACH);
    }

    private class ClientListDataView extends DataView<String> {
        protected ClientListDataView(String id, IDataProvider<String> dataProvider) {
            super(id, dataProvider);
            setItemsPerPage(5);
        }

        @Override
        protected void populateItem(Item<String> item) {
            item.add(new Label("clientUsername", item.getModel()));
            item.add(new Link<Void>("removeClient") {
                @Override
                public void onClick() {
                    userService.removeClient(user, item.getModel().getObject());
                }
            });
        }
    };
}
