package com.javabuilder.backendservice.repository.specification;

import com.javabuilder.backendservice.entity.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> hasEmail(String email) {
        return (root, _, criteriaBuilder) -> {
            if(StringUtils.isBlank(email)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<User> hasDisplayName(String displayName) {
        return (root, _, criteriaBuilder) -> {
            if(StringUtils.isBlank(displayName)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("displayName"), "%" + displayName + "%");
        };
    }
}
