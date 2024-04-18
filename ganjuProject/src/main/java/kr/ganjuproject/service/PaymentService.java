package kr.ganjuproject.service;

import kr.ganjuproject.config.IamportConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final IamportConfig iamportConfig;

    public String getAccessToken() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject request = new JSONObject();
        request.put("imp_key", iamportConfig.iamport().getApikey());
        request.put("imp_secret", iamportConfig.iamport().getSecret());

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JSONObject responseBody = new JSONObject(response.getBody());
        return responseBody.getJSONObject("response").getString("access_token");
    }

    public boolean verifyPayment(String impUid, int totalPrice) throws JSONException {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // 액세스 토큰 사용

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject responseObject = new JSONObject(response.getBody());
        JSONObject responsePayment = responseObject.getJSONObject("response");
        int paidAmount = responsePayment.getInt("amount");

        return paidAmount == totalPrice;
    }
}
