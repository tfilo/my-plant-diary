package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = { "data", "thumbnail","plant" })
@Entity
@Table(name = "pd_photo")
@SequenceGenerator(name = "photo_generator", allocationSize = 1, sequenceName = "pd_photo_seq")
public class Photo implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "photo_generator")
    private Long id;

    @Column(name = "description", nullable = true, length = 200)
    private String description;

    @Column(name = "uploaded", nullable = false)
    private LocalDateTime uploaded;

    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] data;

    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    private byte[] thumbnail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "plant_id", nullable = false, insertable = false, updatable = false)
    private Plant plant;

}
