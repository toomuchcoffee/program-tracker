package de.toomuchcoffee.pt.panel;


import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.toomuchcoffee.pt.service.CoachService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog;
import org.apache.wicket.extensions.ajax.markup.html.modal.theme.DefaultTheme;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static de.toomuchcoffee.pt.domain.entity.Role.COACH;
import static org.apache.wicket.extensions.ajax.markup.html.modal.ModalDialog.CONTENT_ID;

public class CoachPanel extends Panel {

    private final ModalDialog modalDialog;

    private final String coachUsername;

    @SpringBean
    private CoachService coachService;

    public CoachPanel(String id) {
        super(id);

        modalDialog = new ModalDialog("editProgramModal");
        modalDialog.add(new DefaultTheme());
        modalDialog.closeOnClick();
        modalDialog.closeOnEscape();
        add(modalDialog);

        coachUsername = getSession().getAttribute("username").toString();

        DataView<String> dataView = new ClientListDataView("clientList", new ListDataProvider<>() {
            @Override
            protected List<String> getData() {
                return coachService.findAssignedClientsForCoach(coachUsername);
            }
        });

        add(dataView);
        add(new BootstrapPagingNavigator("pagingNavigator", dataView));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("coachHeader", String.format("Coach Dashboard: %s", getSession().getAttribute("username"))));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AbstractAuthenticatedWebSession session = (AbstractAuthenticatedWebSession) getSession();
        setVisible(session.getRoles().hasRole(COACH.name()));
    }


    private class ClientListDataView extends DataView<String> {
        protected ClientListDataView(String id, IDataProvider<String> dataProvider) {
            super(id, dataProvider);
            setItemsPerPage(5);
        }

        @Override
        protected void populateItem(Item<String> item) {
            item.add(new Label("username", new PropertyModel<>(item.getModel(), "")));
            item.add(new AjaxLink<Void>("openProgramDialogLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ProgramPanel programPanel = new ProgramPanel(
                            CONTENT_ID, modalDialog, coachUsername, item.getModel().getObject());
                    modalDialog.open(programPanel, target);
                }
            });
        }
    }

}
