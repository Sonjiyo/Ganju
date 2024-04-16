package kr.ganjuproject.config;

import kr.ganjuproject.dto.IamportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Value("${iamport.key}")
    private String key;
    @Value("${iamport.secret}")
    private String secret;

    @Bean
    public IamportDTO iamport(){
        IamportDTO dto = new IamportDTO();
        dto.setKey(this.key);
        dto.setSecret(this.secret);
        return dto;
    }
}