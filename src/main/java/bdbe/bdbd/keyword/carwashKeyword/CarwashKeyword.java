package bdbe.bdbd.keyword.carwashKeyword;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.keyword.Keyword;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="carwash_keyword")
public class CarwashKeyword { //키워드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="c_id",  nullable = false)
    private Carwash carwash;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="k_id",  nullable = false)
    private Keyword keyword;

    @Builder
    public CarwashKeyword(Long id, Carwash carwash, Keyword keyword) {
        this.id = id;
        this.carwash = carwash;
        this.keyword = keyword;
    }
}