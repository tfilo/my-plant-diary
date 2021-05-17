package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Token")
@Getter
@Setter
@ToString
public class TokenSO {

    private String type = "Bearer";

    private String token;
}
