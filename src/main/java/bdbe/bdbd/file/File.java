package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1024)
    private String url;

    private LocalDateTime uploadedAt;

    @Column(name="is_deleted", nullable = false)
    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "c_id")
    private Carwash carwash;


    @Builder
    public File(String name, String url, LocalDateTime uploadedAt, Carwash carwash) {
        this.name = name;
        this.url = url;
        this.uploadedAt = uploadedAt;
        this.carwash = carwash;

    }

    public File() {
    }

    public void changeDeletedFlag(boolean flag) {
        this.isDeleted = flag;
    }

}
