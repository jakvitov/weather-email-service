package cz.jakvitov.wes.controller;

import cz.jakvitov.wes.scheduler.ScheduledEmailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Rest controller used for testing - it allows us to launch the scheduled
 * operation anytime we call it
 */

@RestController
@RequestMapping("/test")
public class TestRestController {

    private final ScheduledEmailService scheduledEmailService;

    @Autowired
    public TestRestController(ScheduledEmailService scheduledEmailService) {
        this.scheduledEmailService = scheduledEmailService;
    }

    @GetMapping("/weather-schedule")
    public String testEmailScheduling() throws TemplateException, MessagingException, IOException, InterruptedException {
        scheduledEmailService.sendEmailsToActiveUsers();
        return "OK";
    }
}
