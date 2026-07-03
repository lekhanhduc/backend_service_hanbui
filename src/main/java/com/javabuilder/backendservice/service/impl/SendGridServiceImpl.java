package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.service.SendGridService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SENDGRID-SERVICE")
public class SendGridServiceImpl implements SendGridService {

    private static final String FROM = "ducdev212@gmail.com";

    private final SendGrid sendGrid;

    @Override
    public void sendEmail(String to, String displayName, String subject, String templateId) {
        Email from = new Email(FROM);
        Email toEmail = new Email(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        mail.setSubject(subject);
        mail.setSendAt(new Date().toInstant().getEpochSecond());

        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        personalization.addDynamicTemplateData("name", displayName);
        mail.addPersonalization(personalization);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.info("Email sent to {}", to);
            log.info("Response status code: {}", response.getStatusCode());
        } catch (IOException e) {
            log.error("Failed to build mail", e);
        }
    }
}
