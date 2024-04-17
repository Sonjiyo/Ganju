package kr.ganjuproject.config;

import kr.ganjuproject.dto.IamportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Value("${iamport.code}")
    private String code;
    @Value("${iamport.apikey}")
    private String apikey;
    @Value("${iamport.secret}")
    private String secret;

    @Bean
    public IamportDTO iamport(){
        IamportDTO dto = new IamportDTO();
        dto.setCode(this.code);
        dto.setApikey(this.apikey);
        dto.setSecret(this.secret);
        return dto;
    }
}