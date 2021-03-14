package de.toomuchcoffee.pt.panel;

import de.toomuchcoffee.pt.dto.ProgramDto;
import de.toomuchcoffee.pt.service.CoachService;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static org.apache.wicket.feedback.FeedbackMessage.ERROR;

public class ProgramPanel extends Panel {
    private final ModalDialog modalDialog;

    @SpringBean
    private CoachService coachService;

    public ProgramPanel(String id, ModalDialog modalDialog, String coachUsername, String clientUsername) {
        super(id);
        this.modalDialog = modalDialog;
        ProgramDto programDto = coachService.retrieveProgram(coachUsername, clientUsername);
        add(new ProgramForm("programForm", programDto));
    }

    private class ProgramForm extends Form<ProgramDto> {

        public ProgramForm(String id, ProgramDto programDto) {
            super(id);

            setModel(Model.of(programDto));
            FeedbackCollector collector = new FeedbackCollector(this);
            ExactLevelFeedbackMessageFilter errorFilter = new ExactLevelFeedbackMessageFilter(ERROR);
            add(new FeedbackPanel("errorMessages", errorFilter) {
                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(!collector.collect(errorFilter).isEmpty());
                }
            });

            add(new TextField<>("client", PropertyModel.of(getModel(), "client")).setEnabled(false));
            add(new TextField<>("coach", PropertyModel.of(getModel(), "coach")).setEnabled(false));
            add(new TextArea<>("notes", PropertyModel.of(getModel(), "notes")).setRequired(true).add(new PropertyValidator<>()));

            super.onInitialize();
        }

        @Override
        protected void onSubmit() {
            try {
                coachService.saveProgram(getModel().getObject());
                modalDialog.close(null);
            } catch (Exception e) {
                error("Failed to save: " + e.getMessage());
            }
        }
    }
}
