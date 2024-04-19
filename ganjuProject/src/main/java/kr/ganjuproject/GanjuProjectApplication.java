package kr.ganjuproject;

import jakarta.annotation.PostConstruct;
import kr.ganjuproject.config.IamportConfig;
import kr.ganjuproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

@SpringBootApplication
@RequiredArgsConstructor
public class GanjuProjectApplication {
	@PostConstruct
	public void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(GanjuProjectApplication.class, args);
	}

}
