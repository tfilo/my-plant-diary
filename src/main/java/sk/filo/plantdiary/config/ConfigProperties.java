package sk.filo.plantdiary.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ConfigurationProperties("plantdiary")
public class ConfigProperties {

    @NotEmpty
    private String jwtPrivateKey;

    @NotEmpty
    private String jwtPublicKey;

    @NotNull
    private Integer jwtExpirationTime;

    @NotNull
    private String serverEmail;

    @NotNull
    private Boolean asyncExecutor;
}
