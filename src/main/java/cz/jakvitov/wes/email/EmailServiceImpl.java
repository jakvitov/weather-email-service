package cz.jakvitov.wes.email;

import com.sun.mail.util.MailConnectException;
import cz.jakvitov.wes.dto.internal.EmailDto;
import cz.jakvitov.wes.utils.exception.ExceptionUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;}

    @Override
    public void sendEmail(EmailDto emailDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailDto.getFrom());
        messageHelper.setTo(emailDto.getDest());
        messageHelper.setSubject(emailDto.getSubject());
        messageHelper.setText(emailDto.getText(), true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    @Async
    public void sendEmailWithRetryCount(EmailDto emailDto, int retryCount, int delayInMs) throws MessagingException, InterruptedException {
        int attempts = 0;
        while (true) {
            try {
                attempts ++;
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom(emailDto.getFrom());
                messageHelper.setTo(emailDto.getDest());
                messageHelper.setSubject(emailDto.getSubject());
                messageHelper.setText(emailDto.getText(), true);
                javaMailSender.send(mimeMessage);
                break;
            }
            catch (MessagingException messagingException){
                logger.warn("Failed to send a mail. Retrying in " + delayInMs + "ms. Caused by:" + messagingException.getMessage());
                if (!ExceptionUtils.exceptionIsCausedBy(messagingException, MailConnectException.class) || attempts >= retryCount){
                    throw messagingException;
                }
                Thread.sleep(delayInMs);
            }
        }
    }
}
