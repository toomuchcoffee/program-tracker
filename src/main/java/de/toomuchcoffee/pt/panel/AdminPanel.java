package de.toomuchcoffee.pt.panel;

import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.dto.UserDto;
import de.toomuchcoffee.pt.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.extensions.ajax.markup.html.modal.theme.DefaultTheme;
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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

import static de.toomuchcoffee.pt.domain.entity.Role.ADMIN;
import static java.util.stream.Collectors.toList;
import static org.apache.wicket.feedback.FeedbackMessage.ERROR;

public class AdminPanel extends Panel {
    @SpringBean
    private UserService userService;

    private final ModalDialog userEditDialog;

    public AdminPanel(String id) {
        super(id);

        add(new CreateUserForm("createUserForm"));

        userEditDialog = new ModalDialog("editUserModal");
        userEditDialog.add(new DefaultTheme());
        userEditDialog.closeOnClick();
        userEditDialog.closeOnEscape();
        add(userEditDialog);

        DataView<UserDto> dataView = new UserListDataView("userList", new ListDataProvider<>() {
            @Override
            protected List<UserDto> getData() {
                return userService.findAll().stream().map(user -> {
                    UserDto dto = new UserDto();
                    BeanUtils.copyProperties(user, dto);
                    return dto;
                }).collect(toList());
            }
        });

        add(dataView);
        add(new PagingNavigator("pagingNavigator", dataView));
    }


    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole("ADMIN"));
    }

    private class UserListDataView extends DataView<UserDto> {
        protected UserListDataView(String id, IDataProvider<UserDto> dataProvider) {
            super(id, dataProvider);
            setItemsPerPage(5);
        }

        @Override
        protected void populateItem(Item<UserDto> item) {
            item.add(new Label("username", new PropertyModel<>(item.getModel(), "username")));
            item.add(new Label("role", new PropertyModel<>(item.getModel(), "role")));
            item.add(new AjaxLink<Void>("open") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    UserEditPanel editPanel = new UserEditPanel(ModalDialog.CONTENT_ID, userEditDialog, item.getModel().getObject());
                    userEditDialog.open(editPanel, target);
                }
            });
            item.add(new Link<Void>("deleteUser") {
                @Override
                public void onClick() {
                    String username = item.getModel().getObject().getUsername();
                    userService.deleteByUsername(username);
                }
            });
        }
    };

    private class CreateUserForm extends Form<AdminPanel.CreateUserForm> {
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
            List<Role> roles = Arrays.stream(Role.values()).filter(role -> role != ADMIN).collect(toList());
            DropDownChoice<Role> dropDownChoice = new DropDownChoice<>("role", new PropertyModel<>(getModel(), "role"), roles);
            dropDownChoice.setRequired(true);
            add(dropDownChoice);
            super.onInitialize();
        }

        @Override
        protected void onSubmit() {
            userService.save(username, password, role);
        }
    }
}