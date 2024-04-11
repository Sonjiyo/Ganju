package kr.ganjuproject.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    private Map<String, Object> kakao_account;
    public KakaoUserInfo(Map<String, Object> attributes, Map<String, Object> kakao_account){
        System.out.println("attributes = " + attributes);
        this.attributes = attributes;
        this.kakao_account=kakao_account;
    }
    @Override
    public String getProviderId() {
        return attributes.get("id") +"";
    }
    @Override
    public String getProvider() {
        return "kakao";
    }
    @Override
    public String getEmail() {
        return (String)kakao_account.get("email");
    }

    @Override
    public String getPhone() {return "";}
}
