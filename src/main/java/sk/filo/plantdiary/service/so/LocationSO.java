package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(name = "Location")
@Getter
@Setter
@ToString
public class LocationSO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 80)
    private String name;

}
