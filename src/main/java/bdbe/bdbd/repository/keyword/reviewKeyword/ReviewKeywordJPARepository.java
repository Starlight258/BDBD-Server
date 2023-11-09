package bdbe.bdbd.repository.keyword.reviewKeyword;

import bdbe.bdbd.model.keyword.reviewKeyword.ReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewKeywordJPARepository extends JpaRepository<ReviewKeyword, Long> {
    List<ReviewKeyword> findByReview_Id(Long reviewId);
}
