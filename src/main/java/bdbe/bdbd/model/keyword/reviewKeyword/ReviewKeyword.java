package bdbe.bdbd.model.keyword.reviewKeyword;

import bdbe.bdbd.model.keyword.Keyword;
import bdbe.bdbd.model.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_keyword")
public class ReviewKeyword { //키워드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "r_id", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "k_id", nullable = false)
    private Keyword keyword;

    @Builder
    public ReviewKeyword(Long id, Review review, Keyword keyword) {
        this.id = id;
        this.review = review;
        this.keyword = keyword;
    }
}