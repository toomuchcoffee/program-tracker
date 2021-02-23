package de.toomuchcoffee.pt.pages;

import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.service.AuthenticatedUserService;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

import static org.apache.wicket.feedback.FeedbackMessage.ERROR;

@AuthorizeInstantiation("ADMIN")
public class AdminPage extends LayoutPage {
    @SpringBean
    private AuthenticatedUserService authenticatedUserService;


    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CreateUserForm("createUserForm"));

        ListDataProvider<User> userListDataProvider = new ListDataProvider<>() {
            @Override
            protected List<User> getData() {
                return authenticatedUserService.findAll();
            }
        };

        DataView<User> dataView = new DataView<>("userList", userListDataProvider) {
            @Override
            protected void populateItem(Item<User> item) {
                item.add(new Label("username", new PropertyModel<>(item.getModel(), "username")));
                item.add(new Label("role", new PropertyModel<>(item.getModel(), "role")));
                item.add(new Link<Void>("deleteUser") {
                    @Override
                    public void onClick() {
                        String username = item.getModel().getObject().getUsername();
                        authenticatedUserService.deleteByUsername(username);
                    }
                });
            }
        };
        dataView.setItemsPerPage(5L);
        add(dataView);
        add(new PagingNavigator("pagingNavigator", dataView));

    }

    private class CreateUserForm extends Form<AdminPage.CreateUserForm> {
        private String username;
        private String password;
        private Role role;

        public CreateUserForm(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            setModel(new CompoundPropertyModel<>(this));

            FeedbackCollector collector = new FeedbackCollector(this);
            ExactLevelFeedbackMessageFilter errorFilter = new ExactLevelFeedbackMessageFilter(ERROR);
            add(new FeedbackPanel("errorMessages", errorFilter) {
                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(!collector.collect(errorFilter).isEmpty());
                }
            });

            add(new RequiredTextField<String>("username"));
            add(new PasswordTextField("password"));
            List<Role> roles = Arrays.asList(Role.values());
            DropDownChoice<Role> dropDownChoice = new DropDownChoice<>("role", new PropertyModel<>(getModel(), "role"), roles);
            dropDownChoice.setRequired(true);
            add(dropDownChoice);
            super.onInitialize();
        }

        @Override
        protected void onSubmit() {
            authenticatedUserService.save(username, password, role);
        }
    }
}