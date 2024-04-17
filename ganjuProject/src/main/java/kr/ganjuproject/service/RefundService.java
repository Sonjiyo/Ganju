package kr.ganjuproject.service;

import kr.ganjuproject.config.IamportConfig;
import kr.ganjuproject.dto.IamportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundService {

    private final IamportConfig iamportConfig;

    // 인증 토큰 발급 받기
    public String getAccessToken() {
        IamportDTO iDTO = iamportConfig.iamport();

        log.info(iDTO.toString());

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = new HashMap<>();
        System.out.println("imp_key"+ iDTO.getApi());
        System.out.println("imp_key"+ iDTO.getSecret());
        request.put("imp_key", iDTO.getApi());
        request.put("imp_secret", iDTO.getSecret());

        Map response = restTemplate.postForObject("https://api.iamport.kr/users/getToken", request, Map.class);
        Map<String, String> responseMap = (Map<String, String>) response.get("response");
        return responseMap.get("access_token");
    }

    // 환불 처리
    public Map<String, Object> requestRefund(String impUid, String reason, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> request = new HashMap<>();
        request.put("imp_uid", impUid);
        request.put("reason", reason);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject("https://api.iamport.kr/payments/cancel", entity, Map.class);
    }
}