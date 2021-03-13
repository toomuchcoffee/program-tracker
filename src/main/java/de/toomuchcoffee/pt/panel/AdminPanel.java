package de.toomuchcoffee.pt.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.dto.ReadUserDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

import static de.toomuchcoffee.pt.domain.entity.Role.ADMIN;
import static java.util.stream.Collectors.toList;
import static org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog.CONTENT_ID;
import static org.apache.wicket.feedback.FeedbackMessage.ERROR;

public class AdminPanel extends Panel {
    @SpringBean
    private UserService userService;

    private final ModalDialog modalDialog;

    public AdminPanel(String id) {
        super(id);

        add(new CreateUserForm("createUserForm"));

        modalDialog = new ModalDialog("editUserModal");
        modalDialog.add(new DefaultTheme());
        modalDialog.closeOnClick();
        modalDialog.closeOnEscape();
        add(modalDialog);

        DataView<ReadUserDto> dataView = new UserListDataView("userList", new ListDataProvider<>() {
            @Override
            protected List<ReadUserDto> getData() {
                return userService.findAll();
            }
        });

        add(dataView);
        add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }


    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole("ADMIN"));
    }

    private class UserListDataView extends DataView<ReadUserDto> {
        protected UserListDataView(String id, IDataProvider<ReadUserDto> dataProvider) {
            super(id, dataProvider);
            setItemsPerPage(5);
        }

        @Override
        protected void populateItem(Item<ReadUserDto> item) {
            item.add(new Label("username", new PropertyModel<>(item.getModel(), "username")));
            item.add(new Label("role", new PropertyModel<>(item.getModel(), "role")));
            item.add(new AjaxLink<Void>("openUserEditDialogLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    UpdateUserDto updateUserDto = new UpdateUserDto();
                    BeanUtils.copyProperties(item.getModel().getObject(), updateUserDto);
                    UserEditPanel editPanel = new UserEditPanel(CONTENT_ID, modalDialog, updateUserDto);
                    modalDialog.open(editPanel, target);
                }
            });
            item.add(new Link<Void>("deleteUser") {
                @Override
                public void onClick() {
                    String username = item.getModel().getObject().getUsername();
                    userService.delete(username);
                }
            });
        }
    };

    private class CreateUserForm extends Form<CreateUserDto> {

        public CreateUserForm(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            setModel(Model.of(new CreateUserDto()));

            FeedbackCollector collector = new FeedbackCollector(this);
            ExactLevelFeedbackMessageFilter errorFilter = new ExactLevelFeedbackMessageFilter(ERROR);
            add(new FeedbackPanel("errorMessages", errorFilter) {
                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(!collector.collect(errorFilter).isEmpty());
                }
            });

            add(new RequiredTextField<String>("username", PropertyModel.of(getModel(), "username")));
            add(new PasswordTextField("password", PropertyModel.of(getModel(), "password")));
            List<Role> roles = Arrays.stream(Role.values()).filter(role -> role != ADMIN).collect(toList());
            DropDownChoice<Role> dropDownChoice = new DropDownChoice<>("role", PropertyModel.of(getModel(), "role"), roles);
            dropDownChoice.setRequired(true);
            add(dropDownChoice);
            super.onInitialize();
        }

        @Override
        protected void onSubmit() {
            userService.create(getModelObject());
            setModelObject(new CreateUserDto());
        }
    }
}