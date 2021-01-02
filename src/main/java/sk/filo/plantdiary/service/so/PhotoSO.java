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

    public PhotoSO() {}

    public PhotoSO(@Size(max = 200) String description, byte[] data) {
        this.description = description;
        this.data = data;
    }

    @NotNull
    private Long id;

    @Size(max=200)
    private String description;

    private byte[] data;
}
