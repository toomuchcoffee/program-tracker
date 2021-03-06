package de.toomuchcoffee.pt.panel;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
public class AdminPanelTest {
    private WicketTester tester;

    @Autowired
    private WebApplication wicketApplication;

    @Before
    public void setUp() {
        tester = new WicketTester(wicketApplication, new MockServletContext(wicketApplication, null));
    }

    @Test
    public void rendersPanel() {
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signIn("admin", "admin");

        tester.startComponentInPage(AdminPanel.class);

        tester.assertComponent("userList", DataView.class);
        tester.assertComponent("createUserForm", Form.class);
    }
}