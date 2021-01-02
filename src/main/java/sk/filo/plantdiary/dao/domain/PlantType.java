package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "pd_plant_type")
@SequenceGenerator(name = "plant_type_generator", allocationSize = 1, sequenceName = "pd_plant_type_seq")
public class PlantType implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "plant_type_generator")
    private Long id;

    @Column(name = "code", nullable = false, length = 80, unique = true)
    private String code;
}
