package com.javabuilder.backendservice.service;

public interface MailService {
    void sendEmail(String to, String subject, String content);
    void sendEmailTemplate(String to, String username, String subject, String templateName);
}
