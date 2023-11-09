package bdbe.bdbd.repository.review;

import bdbe.bdbd.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long id);

    long countByCarwash_Id(Long carwashId);

    @Query("SELECT r FROM Review r JOIN FETCH r.member WHERE r.carwash.id = :carwashId")
    List<Review> findByCarwash_Id(Long carwashId);

}
