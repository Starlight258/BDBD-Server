package bdbe.bdbd.model.file;

import bdbe.bdbd.model.carwash.Carwash;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1024)
    private String url;

    private LocalDateTime uploadedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "c_id")
    private Carwash carwash;


    @Builder
    public File(Long id, String name, String url, LocalDateTime uploadedAt, Carwash carwash) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.uploadedAt = uploadedAt;
        this.carwash = carwash;
    }

    public void changeDeletedFlag(boolean flag) {
        this.isDeleted = flag;
    }
}
