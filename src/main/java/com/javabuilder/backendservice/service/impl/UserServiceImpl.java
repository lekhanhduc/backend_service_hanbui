package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.common.RoleName;
import com.javabuilder.backendservice.common.UserStatus;
import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.PageResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;
import com.javabuilder.backendservice.entity.Role;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.mapper.UserMapper;
import com.javabuilder.backendservice.repository.UserRepository;
import com.javabuilder.backendservice.repository.specification.UserSpecification;
import com.javabuilder.backendservice.service.MailService;
import com.javabuilder.backendservice.service.RoleService;
import com.javabuilder.backendservice.service.SendGridService;
import com.javabuilder.backendservice.service.UserService;
import com.javabuilder.backendservice.utils.PageResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

import static com.javabuilder.backendservice.constant.EmailSubjectConstant.WELCOME_SUBJECT;
import static com.javabuilder.backendservice.constant.EmailTemplateConstant.WELCOME_TEMPLATE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final MailService mailService;
    private final SendGridService sendGridService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .status(UserStatus.ACTIVE)
                .build();
        try {
            Role defaultRole = roleService.findByOrCreate(RoleName.USER);
            user.addRole(defaultRole);
            userRepository.save(user);

            // send email welcome user
            sendGridService.sendEmail(user.getEmail(), user.getDisplayName(), WELCOME_SUBJECT, "d-6183482f869341db989620a1dca8cec7");
        } catch (DataIntegrityViolationException _) {
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toCreateUserResponse(user);
    }

    @Override
    public UserDetailResponse getUserDetailById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDetailResponse)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.name == #id")
    @Override
    public UserDetailResponse updateUserById(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!Objects.equals(request.displayName(), user.getDisplayName())) {
            userMapper.updateUser(request, user);
            userRepository.save(user);
        }
        return userMapper.toUserDetailResponse(user);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Override
    public void deleteUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public PageResponse<UserDetailResponse> getAllUsers(int page, int size, String email, String displayName) {
        page = PageResponseUtils.normalizePage(page);
        size = PageResponseUtils.normalizeSize(size);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "email"));

        Specification<User> userSpecification = Specification.allOf(UserSpecification.hasEmail(email),
                UserSpecification.hasDisplayName(displayName));

        Page<User> userPage = userRepository.findAll(userSpecification, pageable);

        int totalPages = userPage.getTotalPages();
        long totalElements = userPage.getTotalElements();
        List<User> users = userPage.getContent();
        List<UserDetailResponse> response = users.stream()
                .map(userMapper::toUserDetailResponse)
                .toList();

        return PageResponse.<UserDetailResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(response)
                .build();
    }

    @Override
    public UserDetailResponse getEmailDisplayName(String email) {
        return userRepository.findEmailDisplayNameOnlyByEmail(email)
                .map(emailDisplayNameOnly -> UserDetailResponse.builder()
                        .email(emailDisplayNameOnly.getEmail())
                        .displayName(emailDisplayNameOnly.getDisplayName())
                        .build())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
