package kr.ac.yonsei.fyea;

import kr.ac.yonsei.fyea.service.DataStorageService;
import kr.ac.yonsei.fyea.service.UserService;
import kr.ac.yonsei.fyea.util.CryptographyUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserService userService, DataStorageService dataStorageService) {
		return (args) -> {
			CryptographyUtils.removeCryptographyRestrictions();
			userService.init();
			dataStorageService.initData();
		};
	}
}
