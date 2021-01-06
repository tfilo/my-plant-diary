package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UpdatePhotoSO {

    @NotNull
    private Long id;

    @Size(max = 200)
    private String description;

    private PlantBasicSO plant;
}
