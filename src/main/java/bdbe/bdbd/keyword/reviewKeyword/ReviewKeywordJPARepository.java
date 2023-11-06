package bdbe.bdbd.keyword.reviewKeyword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewKeywordJPARepository extends JpaRepository<ReviewKeyword, Long> {
    List<ReviewKeyword> findByReview_Id(Long reviewId);
    void deleteByReview_Id(Long reviewId);
}
