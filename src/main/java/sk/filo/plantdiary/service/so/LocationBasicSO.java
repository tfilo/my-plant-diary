package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(name = "LocationBasic")
@Getter
@Setter
@ToString
public class LocationBasicSO {

    @NotNull
    private Long id;

    @Size(max = 80)
    private String name;

}
