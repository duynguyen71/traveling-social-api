package com.tc.tcapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final String FROM_ADDRESS = "testting7989@gmail.com";
    private final String SENDER_NAME = "Traveling Crew";

    public void sendVerificationCode(String receivedEmail, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        final String subject = "Account Verification Code";
        String content =
                "Your verification code is:<br>"
                        + "<h2 style=\"color: green; font-weight: bolder\">[[code]]</h2>"
                        + "Thank you,<br>"
                        + "Have a good experience in Traveling Crew.";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(FROM_ADDRESS, SENDER_NAME);
        mimeMessageHelper.setTo(receivedEmail);
        mimeMessageHelper.setSubject(subject);
        content = content.replace("[[code]]", verificationCode);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }


}
