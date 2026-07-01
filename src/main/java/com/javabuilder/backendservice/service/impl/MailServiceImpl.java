package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MAIL-SERVICE")
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine emailTemplateEngine;

    @Async
    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setSentDate(new Date());

        javaMailSender.send(mailMessage);
        log.info("Email sent to {}", to);
    }

    @Async
    @Override
    public void sendEmailTemplate(String to, String username, String subject, String templateName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
        try {
            mimeMessageHelper.setFrom(from, "Backend Service");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(to);

            Context context = new Context();
            context.setVariable("name", username);

            String htmlContent = emailTemplateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent to {} successfully", to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Send email to {} failed", to, e);
        }
    }
}
