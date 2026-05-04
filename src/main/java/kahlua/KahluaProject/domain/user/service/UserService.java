package kahlua.KahluaProject.domain.user.service;

import kahlua.KahluaProject.domain.user.dto.request.UserProfileRequest;
import kahlua.KahluaProject.domain.user.dto.response.UserListResponse;
import kahlua.KahluaProject.domain.user.dto.response.UserProfileResponse;
import kahlua.KahluaProject.domain.user.entity.ApprovalFilter;
import kahlua.KahluaProject.domain.user.entity.Credential;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.user.converter.UserConverter;
import kahlua.KahluaProject.domain.user.dto.request.SignUpRequest;
import kahlua.KahluaProject.domain.user.dto.request.UserInfoRequest;
import kahlua.KahluaProject.domain.user.dto.response.UserResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(Credential credential, SignUpRequest signUpRequest) {
        userRepository.findByEmailAndDeletedAtIsNull(signUpRequest.getEmail()).ifPresent(existingUser -> {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_USER);
        });
        User user = signUpRequest.toUser(credential);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Transactional
    public void withdraw(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public UserResponse createUserInfo(Long userId, UserInfoRequest userInfoRequest) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.updateUserInfo(userInfoRequest);

        return UserConverter.toUserResDto(user);
    }

    @Transactional
    public UserListResponse getUserList(ApprovalFilter approvalFilter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        switch (approvalFilter) {
            case PENDING ->
                    userPage = userRepository.findAllByUserType(UserType.PENDING, pageable);
            case APPROVED ->
                    userPage = userRepository.findAllByUserTypeIn(
                            List.of(UserType.KAHLUA, UserType.ADMIN), pageable);
            case ALL ->
                    userPage = userRepository.findAllByUserTypeNot(UserType.GENERAL, pageable);
            default ->
                    throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
        }

        long pendingCount  = userRepository.countByUserType(UserType.PENDING);
        long approvedCount = userRepository.countByUserTypeIn(List.of(UserType.KAHLUA, UserType.ADMIN));

        return UserListResponse.of(userPage, pendingCount, approvedCount);

    }

    @Transactional
    public UserResponse changeUserType(Long userId, UserType newType) {
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateUserType(newType);
        userRepository.save(user);

        return UserConverter.toUserResDto(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfileImage(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.toUserProfileResDto(user);
    }

    @Transactional
    public UserProfileResponse updateUserProfileImage(Long userId, UserProfileRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateUserProfileImageUrl(request.getProfileImageUrl());

        return UserConverter.toUserProfileResDto(user);
    }

    @Transactional
    public UserProfileResponse deleteUserProfileImage(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (user.getProfileImageUrl() == null) {
            throw new GeneralException(ErrorStatus.ALREADY_DELETED_PROFILE_IMAGE);
        }
        user.updateUserProfileImageUrl((String) null);

        return UserConverter.toUserProfileResDto(user);
    }
}
