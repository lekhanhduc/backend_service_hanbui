package com.javabuilder.backendservice.repository.projection;

public interface EmailDisplayNameOnly {
    String getEmail();
    String getDisplayName();

    default String getUsername() {
        return getEmail();
    }
}
