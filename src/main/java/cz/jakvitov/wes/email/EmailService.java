package cz.jakvitov.wes.email;

import cz.jakvitov.wes.dto.internal.EmailDto;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmail(EmailDto emailDto) throws MessagingException;

}
