package kr.ganjuproject.auth;

import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.oauth.provider.GoogleUserInfo;
import kr.ganjuproject.oauth.provider.KakaoUserInfo;
import kr.ganjuproject.oauth.provider.NaverUserInfo;
import kr.ganjuproject.oauth.provider.OAuth2UserInfo;
import kr.ganjuproject.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final ManagerRepository managerRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // userQuest 는 구글에서 코드를 받아서 accessToken을 응답 받는객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("user request clientRegistration : " + userRequest.getClientRegistration());
        System.out.println("user reuset getAccessToken :" + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest); // google 회원 프로필 조회
        System.out.println("get Attribute : " + oAuth2User.getAttributes());
        // loadUser --> Authentication 객체 안에 들어간다
        return processOAuthUser(userRequest, oAuth2User);
    }

    private OAuth2User processOAuthUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        System.out.println("userRequest = " + userRequest.getClientRegistration().getRegistrationId());
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes(), (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account"));
        } else {
            System.out.println("요청 실패 ");
        }
        //System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
        //System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
        Optional<Users> userOptional =
                managerRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Users user;
        if (!userOptional.isPresent()) {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = Users.builder()
                    .loginId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .phone(oAuth2UserInfo.getPhone())
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            managerRepository.save(user);
        } else {
            user = userOptional.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}