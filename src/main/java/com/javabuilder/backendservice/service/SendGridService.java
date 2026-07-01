package com.javabuilder.backendservice.service;

public interface SendGridService {
    void sendEmail(String to, String displayName, String subject, String templateId);
}
