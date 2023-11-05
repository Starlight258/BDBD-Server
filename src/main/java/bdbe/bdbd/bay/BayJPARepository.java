package bdbe.bdbd.bay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BayJPARepository extends JpaRepository<Bay, Long> {
    Bay findFirstBy();  // 테스트시에 사용

    @Query("SELECT b.id FROM Bay b WHERE b.carwash.id = :carwashId")
    List<Long> findIdsByCarwashId(@Param("carwashId") Long carwashId); // 세차장 id로 베이 찾고 id 리스트 생성

    List<Bay> findByCarwashId(Long carwashId);

    Optional<Object> findAllById(Long bayId);

}
