package com.oauth2.social.login.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.oauth2.social.login.entity.Role;
import com.oauth2.social.login.entity.UserInfo;
import com.oauth2.social.login.enums.AuthProvider;
import com.oauth2.social.login.enums.ERole;
import com.oauth2.social.login.model.user.OAuth2UserInfo;
import com.oauth2.social.login.factory.OAuth2UserInfoFactory;
import com.oauth2.social.login.model.user.UserPrincipal;
import com.oauth2.social.login.repository.RoleRepository;
import com.oauth2.social.login.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws Exception {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isBlank (oAuth2UserInfo.getEmail())) {
            throw new Exception ("Email not found from OAuth2 provider");
        }

        Optional<UserInfo> userOptional = userInfoRepository.findByUserEmail(oAuth2UserInfo.getEmail());
        UserInfo userInfo;
        if(userOptional.isPresent()) {
            userInfo = userOptional.get();
            if(!userInfo.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new Exception("Looks like you're signed up with " +
                        userInfo.getProvider() + " account. Please use your " + userInfo.getProvider() +
                        " account to login.");
            }
            userInfo = updateExistingUser(userInfo, oAuth2UserInfo);
        } else {
            userInfo = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(userInfo, oAuth2User.getAttributes());
    }

    private UserInfo registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        UserInfo user = new UserInfo();

        user.setId(UUID.randomUUID().toString());
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUsername(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setEnabled(true);

        Role role_user = roleRepository.getRoleByName(ERole.ROLE_USER);
        Set<Role> roles = user.getRoles();
        roles.add(role_user);

        return userInfoRepository.save(user);
    }

    private UserInfo updateExistingUser(UserInfo existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userInfoRepository.save(existingUser);
    }

}
