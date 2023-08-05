package cz.jakvitov.wes.scheduler;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class EmailSchedulerServiceTest {

    @Autowired
    private ScheduledEmailService scheduledEmailService;

    @Test
    public void testScheduledEmailSending() throws TemplateException, MessagingException, IOException {
        scheduledEmailService.sendEmailsToActiveUsers();
    }

}
