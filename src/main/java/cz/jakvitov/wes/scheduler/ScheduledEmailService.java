package cz.jakvitov.wes.scheduler;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface ScheduledEmailService {

    public void sendEmailsToActiveUsers() throws TemplateException, IOException, MessagingException;


}
