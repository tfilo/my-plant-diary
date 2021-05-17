package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(name = "CreateEvent")
@Getter
@Setter
@ToString
public class CreateEventSO {

    @NotNull
    @Valid
    private EventTypeSO type;

    @NotNull
    private LocalDateTime dateTime;

    @Size(max = 1000)
    private String description;

    @NotNull
    @Valid
    private PlantBasicSO plant;

}
