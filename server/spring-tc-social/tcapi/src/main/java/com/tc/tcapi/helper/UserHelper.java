
package com.tc.tcapi.helper;

import com.tc.core.exception.FileNotFoundException;
import com.tc.core.exception.FileUploadException;
import com.tc.tcapi.model.FileUpload;
import com.tc.tcapi.model.Follow;
import com.tc.tcapi.model.User;
import com.tc.core.request.*;
import com.tc.core.response.*;
import com.tc.core.utilities.ValidationUtil;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileUrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("userHelper")
@RequiredArgsConstructor
@Slf4j
public class UserHelper {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final FollowService followService;
    private final JwtService jwtService;

    public ResponseEntity<?> searchingUsers(Map<String, String> params) {
        BaseParamRequest baseParam = new BaseParamRequest(params);
        String pUsername = params.get("username");
        String pFullName = params.get("fullName");
        String pPhone = params.get("phone");
        String pEmail = params.get("email");
        String username = null;
        String fullName = null;
        String email = null;
        String phone = null;
        if (!ValidationUtil.isNullOrBlank(pUsername))
            username = "%" + pUsername.trim() + "%";

        if (!ValidationUtil.isNullOrBlank(pFullName))
            fullName = "%" + pFullName.trim() + "%";

        if (!ValidationUtil.isNullOrBlank(pEmail))
            email = "%" + pEmail.trim() + "%";

        if (!ValidationUtil.isNullOrBlank(pPhone))
            phone = "%" + pPhone.trim() + "%";

        Pageable pageable = baseParam.toPageRequest();

        List<User> users = userService.search(username, fullName, phone, email, 1, pageable);

        User user = userService.getCurrentUser();
        List<UserInfoResponse> data = users.stream()
                .map(u -> {
                    UserInfoResponse userInfoResponse = modelMapper.map(u, UserInfoResponse.class);
                    userInfoResponse.setIsFollowing(followService.isFollowed(u, user, 1));
                    return userInfoResponse;
                })
                .collect(Collectors.toList());
        return BaseResponse.success(data, "Searching users success!");
    }

    public ResponseEntity<?> registrationAccount(RegistrationRequest registrationRequest) {
        String usernameRequest = registrationRequest.getUsername().trim();
        String emailRequest = registrationRequest.getEmail().trim();
        String passwordRequest = registrationRequest.getPassword().trim();

        if (userService.existByEmail(emailRequest))
            return BaseResponse.conflict("Email has been used");
        if (userService.existByUsername(usernameRequest))
            return BaseResponse.conflict("Username has been used");
        if (passwordRequest.length() < 6) {
            return BaseResponse.badRequest("Password is not valid");
        }
        User user = new User();
        try {
            String verificationCode = userService.generateUniqueCode();
            mailService.sendVerificationCode(emailRequest, verificationCode);
            user.setVerificationCode(verificationCode);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        user.setUsername(usernameRequest);
        user.setEmail(emailRequest);
        user.setPassword(passwordEncoder.encode(passwordRequest));
        user.setActive(0);
        user.setRole(roleService.getMemberRole());
        userService.save(user);
        return BaseResponse.success(null, "Registration user success!");
    }


    public ResponseEntity<?> updateAvt(MultipartFile file) {
        try {
            FileUploadResponse fileUploadResponse = fileStorageService.saveImage(file);
            //save user avt
            User user = userService.getCurrentUser();
            user.setAvt(fileUploadResponse.getName());
            userService.save(user);
            return BaseResponse.success(fileUploadResponse, "Update avatar success!");
        } catch (FileUploadException e) {
            return BaseResponse.badRequest(e.getMessage());
        }
    }

    public ResponseEntity<?> updateBackground(MultipartFile file) {
        try {
            FileUploadResponse fileUploadResponse = fileStorageService.saveImage(file);
            //save user avt
            User user = userService.getCurrentUser();
            user.setBackground(fileUploadResponse.getName());
            userService.save(user);
            return BaseResponse.success(fileUploadResponse, "Update background success!");
        } catch (FileUploadException e) {
            return BaseResponse.badRequest(e.getMessage());
        }
    }

    public ResponseEntity<?> getImage(String fileName) {
        //check file upload info exist in db
        FileUpload fileUpload = fileStorageService.getFileUpload(fileName);
        if (fileUpload != null && fileUpload.getActive() == 1
                && (fileUpload.getContentType().equals("image/jpeg") || fileUpload.getContentType().equals("image/png"))) {
            //return file as resource
            try {
                File file = fileStorageService.getFileFromStorage(fileName);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new FileUrlResource(file.getPath()));
            } catch (FileNotFoundException | MalformedURLException e) {
                log.info("Can not find image with name: {}", fileName);
//                e.printStackTrace();
                return BaseResponse.badRequest(e.getMessage());
            }
        }
        return BaseResponse.badRequest("Could not find file with name : " + fileName);
    }

    public ResponseEntity<?> verificationAccount(String code) {
        User user = userService.getByCode(code);
        if (user == null)
            return BaseResponse.badRequest("Code is not valid");
        user.setVerificationCode(null);
        user.setActive(1);
        userService.save(user);
        String accessToken = jwtService.generateToken(user, 15);
        String refreshToken = jwtService.generateToken(user, 30);
        return BaseResponse.
                success(Map.of("accessToken", accessToken, "refreshToken", refreshToken),
                        "Active account success!");

    }

    public ResponseEntity<?> updateCurrentUserInfo(UserUpdateRequest userUpdateRequest) {
        String username = userUpdateRequest.getUsername();
        String fullName = userUpdateRequest.getFullName();
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getUsername().equals(username)
                && userService.existByUsername(username))
            currentUser.setUsername(username);
        currentUser.setFullName(fullName);
        userService.save(currentUser);
        return BaseResponse.success(null, "update current user info success!");
    }

    public ResponseEntity<?> validationInput(String input, String value) {
        if (!ValidationUtil.isNullOrBlank(input)) {
            value = value.trim();
            if (ValidationUtil.isNullOrBlank(value)) {
                return BaseResponse.badRequest(input + " may not be blank!");
            }
            switch (input) {
                case "username": {
                    if (value.length() < 4 || value.length() > 15) {
                        return BaseResponse.badRequest("Username must between 4 and 15");
                    } else if (userService.existByUsername(value)) {
                        return BaseResponse.conflict("Username has been used!");
                    }
                    break;
                }
                case "email": {
                    if (ValidationUtil.isEmail(value)) {
                        if (userService.existByEmail(value)) {
                            return BaseResponse.conflict("Email has been used");
                        }
                        break;
                    } else {
                        return BaseResponse.badRequest("Email is not valid!");
                    }
                }
                case "phone": {
                    break;
                }
                default:
                    return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getFollowingUsers(Map<String, String> param) {
        BaseParamRequest baseParam = new BaseParamRequest(param);
        List<Follow> follows = followService.getFollowingUsers(userService.getCurrentUser().getId(), baseParam.toPageRequest());
        List<UserInfoResponse> rs = follows.stream()
                .map(f -> modelMapper.map(f.getUser(), UserInfoResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get following users success!");
    }

    public ResponseEntity<?> getFollowers(Map<String, String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        User currentUser = userService.getCurrentUser();
        List<Follow> follows = followService
                .getFollowers(currentUser.getId(), baseParamRequest.toPageRequest());
        List<UserInfoResponse> rs = follows.stream()
                .map(f -> {
                    UserInfoResponse map = modelMapper.map(f.getFollower(), UserInfoResponse.class);
                    // check current user follow f.User()
                    boolean i = followService.isFollowed(f.getFollower(), currentUser, 1);
                    map.setIsFollowing(i);
                    return map;
                })
                .toList();
        return BaseResponse.success(rs, "get followers success!");
    }

    public ResponseEntity<?> followUser(Long userId) {
        User followed = userService.getById(userId);
        User currentUser = userService.getCurrentUser();
        if (!Objects.equals(currentUser.getId(), userId) && followed != null) {
            Follow follow;
            if ((follow = followService.getByUserAndFollower(followed, currentUser)) == null) {
                follow = new Follow();
                follow.setFollower(currentUser);
                follow.setUser(followed);
            }
            follow.setActive(1);
            follow.setStatus(1);
            followService.saveAndFlush(follow);
            return BaseResponse.success(null, "Following user " + userId + " request success!");
        }
        return BaseResponse.badRequest("Can not follow user with id: " + userId);
    }

    public ResponseEntity<?> unFollowUser(Long id) {
        log.info("USER : " + id);
        log.info("FOLLOWER : " + userService.getCurrentUser().getId());
        Follow follow = followService.getByUserAndFollower(userService.getById(id), userService.getCurrentUser());
        if (follow != null) {
            follow.setActive(0);
            follow.setStatus(0);
            followService.saveAndFlush(follow);
            return BaseResponse.success(null, "unfollow user with id: " + id + "  success!");
        }
        return BaseResponse.badRequest("Can not unfollow user with id: " + id);
    }

    public ResponseEntity<?> getCurrentUserDetail() {
        User user = userService.getById(userService.getCurrentUser().getId());
        UserDetailResponse detail = modelMapper.map(user, UserDetailResponse.class);
        int followerCounts = followService.countFollowing(user, 1);
        int followingCounts = followService.countFollower(user, 1);
        detail.setFollowerCounts(followerCounts);
        detail.setFollowingCounts(followingCounts);
        return BaseResponse.success(detail, "get current user detail success!");
    }


    public ResponseEntity<?> getUserProfile(Long userId) {
        User user = userService.getById(userId, 1);
        if (user == null)
            return BaseResponse.badRequest("Can not find user with id: " + userId);
        UserProfileResponse userInfoResponse = modelMapper.map(user, UserProfileResponse.class);
        int followerCounts = followService.countFollowing(user, 1);
        int followingCounts = followService.countFollower(user, 1);
        userInfoResponse.setFollowerCounts(followerCounts);
        userInfoResponse.setFollowingCounts(followingCounts);
        userInfoResponse.setIsFollowing(followService.isFollowed(user, userService.getCurrentUser(), 1));
        return BaseResponse.success(userInfoResponse, "Get user with id: " + userId + " success");
    }

    public ResponseEntity<?> getTopUsers(Map<String, String> param) {
        Pageable pageable = new BaseParamRequest(param).toPageRequest();
        List<User> users = userService.getTopActiveUsers(pageable);
        List<UserInfoResponse> rs = users.stream()
                .map(u -> modelMapper.map(u, UserInfoResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(rs, "get top posts users success");

    }

    public ResponseEntity<?> setUsingApp(Integer status) {
        User currentUser = userService.getCurrentUser();
        if (status < 0 || status > 1) {
            return BaseResponse.badRequest("Using app status is not valid");
        }
        currentUser.setIsUsingApp(status);
        userService.save(currentUser);
        return BaseResponse.success("Change user using app status to " + status);
    }

    /**
     * Reset current user password
     */
    public ResponseEntity<?> resetCurrentUserPassword(ResetPasswordRequest request) {
        User currentUser = userService.getCurrentUser();
        // check verification code match
        String verificationCode = currentUser.getVerificationCode();
        if (!Objects.equals(verificationCode, request.getVerificationCode()))
            return BaseResponse.badRequest("Verification code is not match");
        //
        boolean matchOldPass = passwordEncoder.matches(currentUser.getPassword(), request.getOldPassword());
        if (!matchOldPass) {
            return BaseResponse.badRequest("Old password not correct!");
        }
        String encodedPass = passwordEncoder.encode(request.getNewPassword());
        currentUser.setPassword(encodedPass);
        userService.save(currentUser);
        return BaseResponse.success("Reset password success");

    }

    /**
     * Generating new verification code
     */
    public ResponseEntity<?> requestVerificationCode() {
        User currentUser = userService.getCurrentUser();
        // generating new verification code
        try {
            String code = userService.generateUniqueCode();
            currentUser.setVerificationCode(code);
            mailService.sendVerificationCode(currentUser.getEmail(), code);
            userService.save(currentUser);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseResponse.success("Request verification code success!");
    }

    /**
     * Get user has role ROLE_MEMBER
     */
    public ResponseEntity<?> getMemberUsers() {
        List<User> users = userService.getUsersHasRole(roleService.getMemberRole());
        List<BaseUserResponse> data = users.stream()
                .map(user -> modelMapper.map(user, BaseUserResponse.class))
                .collect(Collectors.toList());
        return BaseResponse.success(data, "get member users success!");
    }

    /**
     * Update base user info
     */
    public ResponseEntity<?> updateBaseUserInfo(UpdateBaseInfoRequest request) {
        String newUsername = request.getUsername();
        User user = userService
                .getCurrentUser();
        if (!ValidationUtil.isNullOrBlank(newUsername)
                && !Objects.equals(user.getUsername(), newUsername)
                && !userService.existByUsername(newUsername)) {
            user.setUsername(newUsername);
        }
        String newBio = request.getBio();
        if (!ValidationUtil.isNullOrBlank(newBio)) {
            user.setBio(newBio);
        }
        String newWebsite = request.getWebsite();
        if (!ValidationUtil.isNullOrBlank(newWebsite)) {
            user.setWebsite(newWebsite);
        }
        String newFullName = request.getFullName();
        if (!ValidationUtil.isNullOrBlank(request.getFullName())) {
            user.setFullName(newFullName);
        }
        Date birthdate = ValidationUtil.tryParseDate(request.getBirthdate());
        if (birthdate != null) {
            user.setBirthdate(birthdate);
        }
        userService.save(user);
        return BaseResponse.success("Update base user info success!");
    }
}
