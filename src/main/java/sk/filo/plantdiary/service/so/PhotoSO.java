package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(exclude = "data")
public class PhotoSO {

    @NotNull
    private Long id;
    @Size(max = 200)
    private String description;
    private byte[] data;
    private PlantBasicSO plant;

    public PhotoSO() {
    }

    public PhotoSO(CreatePhotoSO photo, byte[] data) {
        this.description = photo.getDescription();
        this.data = data;
        this.plant = photo.getPlant();
    }
}
