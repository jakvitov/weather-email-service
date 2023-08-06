package cz.jakvitov.wes.email;

import cz.jakvitov.wes.dto.internal.EmailDto;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmail(EmailDto emailDto) throws MessagingException;

    /**
     * If sending email fails due to connection problems, the method will repeat for given amount of attempts after given delay.
     * @param emailDto
     * @param retryCount
     * @param delayInMs
     * @throws MessagingException
     */
    public void sendEmailWithRetryCount(EmailDto emailDto, int retryCount, int delayInMs) throws MessagingException, InterruptedException;

}
