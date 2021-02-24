package de.toomuchcoffee.pt.page;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.util.tester.FormTester;
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
public class LoginPageTest {
    private WicketTester tester;

    @Autowired
    private WebApplication wicketApplication;

    @Before
    public void setUp() {
        tester = new WicketTester(wicketApplication, new MockServletContext(wicketApplication, null));
    }

    @Test
    public void shouldShowLoginPageWhenNotLoggedIn() {
        tester.executeUrl("");
        tester.assertRenderedPage(LoginPage.class);
    }

    @Test
    public void shouldLogin() {
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signOut();
        tester.startPage(LoginPage.class);
        FormTester formTester = tester.newFormTester("loginForm");
        formTester.setValue("username", "admin");
        formTester.setValue("password", "admin");
        formTester.submit();
        tester.assertNoErrorMessage();
        tester.assertRenderedPage(DashboardPage.class);
    }

    @Test
    public void shouldRejectInvalidCredentials() {
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signOut();
        tester.startPage(LoginPage.class);
        FormTester formTester = tester.newFormTester("loginForm");
        formTester.setValue("username", "wrong_user");
        formTester.setValue("password", "wrong_password");
        formTester.submit();
        tester.assertErrorMessages("Login failed");
        tester.assertRenderedPage(LoginPage.class);
    }

}