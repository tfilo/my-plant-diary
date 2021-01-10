package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ScheduleSO {

    @NotNull
    private Long id;

    @NotNull
    @Valid
    private PlantBasicSO plant;

    @NotNull
    @Valid
    private EventTypeSO type;

    @Min(1)
    @Max(365)
    @Digits(integer = 3, fraction = 0)
    private Integer repeatEvery;

    @NotNull
    private Boolean autoUpdate;

    @NotNull
    private LocalDate next;
}
