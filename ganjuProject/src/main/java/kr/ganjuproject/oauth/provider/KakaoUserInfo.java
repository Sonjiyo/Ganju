package kr.ganjuproject.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    public KakaoUserInfo(Map<String, Object> attributes){this.attributes = attributes;}
    @Override
    public String getProviderId() {
        return (String)attributes.get("sub");
    }
    @Override
    public String getProvider() {
        return "google";
    }
    @Override
    public String getEmail() {
        return (String)attributes.get("account_email");
    }

    @Override
    public String getPhone() {return "";}
}
