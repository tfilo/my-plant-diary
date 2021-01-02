package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(exclude = {"password"})
public class AuthSO {

    @NotBlank
    @Size(max = 25)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;
}
