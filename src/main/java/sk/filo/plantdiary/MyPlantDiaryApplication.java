package sk.filo.plantdiary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sk.filo.plantdiary.config.ConfigProperties;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

@SpringBootApplication(scanBasePackages = "sk.filo.plantdiary", exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties({ConfigProperties.class})
@EnableAsync
public class MyPlantDiaryApplication {

    @Autowired
    ConfigProperties configProperties;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SpringApplication.run(MyPlantDiaryApplication.class, args);
    }

    @Bean("pdTaskExecutor") // specific executor
    public Executor algoExecutor() {
        if (!configProperties.getAsyncExecutor()) {
            return new SyncTaskExecutor();
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("pd-");
        executor.initialize();
        return executor;
    }

}
