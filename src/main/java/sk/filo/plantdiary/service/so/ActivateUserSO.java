package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
