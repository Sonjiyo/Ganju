package kr.ganjuproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ganjuproject.config.IamportConfig;
import kr.ganjuproject.dto.IamportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final IamportConfig iamportConfig;
    private final ObjectMapper objectMapper; // ObjectMapper 인스턴스 주입

    // 인증 토큰 발급 받기
    public String getAccessToken() throws IOException {
        IamportDTO iDTO = iamportConfig.iamport();
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // JSON 객체 생성 및 데이터 추가
        String jsonInputString = objectMapper.writeValueAsString(Map.of(
                "imp_key", iDTO.getApikey(),
                "imp_secret", iDTO.getSecret()
        ));

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            String accessToken = jsonNode.path("response").path("access_token").asText();
            return accessToken;
        }
    }

    public boolean verifyPayment(String impUid, int totalPrice) throws IOException {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JsonNode responseObject = objectMapper.readTree(response.getBody());
        JsonNode responsePayment = responseObject.path("response");
        int paidAmount = responsePayment.path("amount").asInt();
        String status = responsePayment.path("status").asText();

        // 결제 상태가 "paid"이고, 금액이 일치하는 경우에만 true 반환
        return "paid".equals(status) && paidAmount == totalPrice;
    }
}
