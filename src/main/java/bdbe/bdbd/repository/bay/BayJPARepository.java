package bdbe.bdbd.repository.bay;

import bdbe.bdbd.model.bay.Bay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BayJPARepository extends JpaRepository<Bay, Long> {
    Bay findFirstBy();

    @Query("SELECT b.id FROM Bay b WHERE b.carwash.id = :carwashId")
    List<Long> findIdsByCarwashId(@Param("carwashId") Long carwashId);

    List<Bay> findByCarwashId(Long carwashId);

}
