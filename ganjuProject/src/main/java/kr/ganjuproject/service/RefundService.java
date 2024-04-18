package kr.ganjuproject.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import kr.ganjuproject.config.IamportConfig;
import kr.ganjuproject.dto.IamportDTO;
import kr.ganjuproject.dto.OrderResponseDTO;
import kr.ganjuproject.entity.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundService {

    private final IamportConfig iamportConfig;
    private final OrdersService ordersService; // OrdersService 주입
    // controller 에서 그냥 불러와서 써지네?
    private final SimpMessagingTemplate messagingTemplate;

    // 인증 토큰 발급 받기
    public String getAccessToken() throws IOException {
        IamportDTO iDTO = iamportConfig.iamport();
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");

        // 요청의 Content-Type과 Accept 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("imp_key", iDTO.getApikey());
        json.addProperty("imp_secret", iDTO.getSecret());

        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString()); // json 객체를 문자열 형태로 HTTP 요청 본문에 추가
        bw.flush(); // BufferedWriter 비우기
        bw.close(); // BufferedWriter 종료

        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
        br.close(); // BufferedReader 종료

        conn.disconnect(); // 연결 종료

        log.info("Iamport 엑세스 토큰 발급 성공 : {}", accessToken);
        return accessToken;
    }

    // 환불 처리
    public Map<String, Object> requestRefund(Orders order, String reason, String accessToken) {
        try {
            URL url = new URL("https://api.iamport.kr/payments/cancel");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // HTTP 메서드 설정
            connection.setRequestMethod("POST");

            // 헤더 설정
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", accessToken);

            // 요청 본문 전송을 위해 출력 가능으로 설정
            connection.setDoOutput(true);

            // 요청 본문 구성
            String jsonInputString = "{\"imp_uid\": \"" + order.getUid() + "\", \"reason\": \"" + reason + "\"}";

            // 요청 본문 전송
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 응답 수신
            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // 성공적인 응답 처리
                System.out.println("성공");
                ordersService.deleteOrder(order.getId());
                // 응답 본문을 JSON 객체로 파싱하고 필요한 정보를 추출/가공하여 반환
                OrderResponseDTO dto = ordersService.convertToOrderResponseDTO(order);

                messagingTemplate.convertAndSend("/topic/calls", order.getUid());
                return Map.of("success", true);
            } else {
                System.out.println("POST request not worked");
                // 오류 처리
                return Map.of("success", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
}