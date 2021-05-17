package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "ActivateUser")
@Getter
@Setter
@ToString
public class ActivateUserSO {

    @NotBlank
    @Size(min = 5, max = 25)
    private String username;

    @NotBlank
    private String token;
}
