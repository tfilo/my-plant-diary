package sk.filo.plantdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sk.filo.plantdiary.config.ConfigProperties;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication(scanBasePackages = "sk.filo.plantdiary", exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties(ConfigProperties.class)
public class MyPlantDiaryApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(MyPlantDiaryApplication.class, args);
	}

}
