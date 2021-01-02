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
@Table(name = "pd_location")
@SequenceGenerator(name = "location_generator", allocationSize = 1, sequenceName = "pd_location_seq")
public class Location implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "location_generator")
    private Long id;

    @Column(name = "name", nullable = false, length = 80, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "owner_username", nullable = false)
    private User owner;
}
