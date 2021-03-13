package de.toomuchcoffee.pt.panel;

import de.toomuchcoffee.pt.dto.UpdateUserDto;
import de.toomuchcoffee.pt.page.DashboardPage;
import de.toomuchcoffee.pt.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static org.apache.wicket.feedback.FeedbackMessage.ERROR;

public class UserEditPanel extends Panel {
    private final ModalDialog modalDialog;
    private final UpdateUserDto user;
    private final boolean coachMode;

    @SpringBean
    private UserService userService;

    public UserEditPanel(String id, ModalDialog modalDialog, UpdateUserDto user, boolean coachMode) {
        super(id);
        this.modalDialog = modalDialog;
        this.user = user;
        this.coachMode = coachMode;
        add(new EditUserForm("editUserForm"));
    }

    private class EditUserForm extends Form<EditUserForm> {

        public EditUserForm(String id) {
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

            add(new RequiredTextField<String>("username", PropertyModel.of(user, "username")).setEnabled(false));
            add(new TextField<String>("fullName", PropertyModel.of(user, "fullName")));
            add(new PasswordTextField("password", PropertyModel.of(user, "password")).setRequired(false));
            add(new ClientsPanel("clientsPanel", user, coachMode));
            add(new AjaxButton("deleteButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    userService.delete(user.getUsername());
                    modalDialog.close(target);
                    setResponsePage(DashboardPage.class);
                }

                @Override
                protected void updateAjaxAttributes( AjaxRequestAttributes attributes ) {
                    super.updateAjaxAttributes( attributes );
                    AjaxCallListener ajaxCallListener = new AjaxCallListener();
                    ajaxCallListener.onPrecondition( "return confirm('Are you sure?');" );
                    attributes.getAjaxCallListeners().add( ajaxCallListener );
                }
            });
            super.onInitialize();
        }

        @Override
        protected void onSubmit() {
            try {
                userService.update(user);
                modalDialog.close(null);
            } catch (Exception e) {
                error("Failed to save: " + e.getMessage());
            }
        }
    }
}
