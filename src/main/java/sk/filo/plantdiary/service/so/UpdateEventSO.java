package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UpdateEventSO {

    @NotNull
    private Long id;

    @NotNull
    @Valid
    private EventTypeSO type;

    @NotNull
    private LocalDateTime dateTime;

    @Size(max = 1000)
    private String description;

}
