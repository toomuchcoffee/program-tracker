package de.toomuchcoffee.pt.page;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.panel.AdminPanel;
import de.toomuchcoffee.pt.panel.ClientPanel;
import de.toomuchcoffee.pt.panel.CoachPanel;
import de.toomuchcoffee.pt.service.UserService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
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
public class DashboardPageTest {
    private WicketTester tester;

    @Autowired
    private WebApplication wicketApplication;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        tester = new WicketTester(wicketApplication, new MockServletContext(wicketApplication, null));

        userService.save(CreateUserDto.builder().username("coach").password("coach").role(Role.COACH).build());
        userService.save(CreateUserDto.builder().username("client").password("client").role(Role.CLIENT).build());
    }

    @Test
    public void shouldShowLoginPageWhenNotLoggedIn() {
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(LoginPage.class);
    }

    @Test
    public void shouldLoginAsAdmin(){
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signIn("admin", "admin");

        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
        tester.assertComponent("adminPanel", AdminPanel.class);
    }

    @Test
    public void shouldLoginAsCoach(){
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signIn("coach", "coach");

        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
        tester.assertComponent("coachPanel", CoachPanel.class);
        tester.assertNotExists("adminPanel");
    }

    @Test
    public void shouldLoginAsClient(){
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signIn("client", "client");

        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
        tester.assertComponent("clientPanel", ClientPanel.class);
        tester.assertNotExists("adminPanel");
    }
}