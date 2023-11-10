package bdbe.bdbd.repository.optime;

import bdbe.bdbd.model.optime.Optime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptimeJPARepository extends JpaRepository<Optime, Long> {
    List<Optime> findByCarwash_Id(Long carwashId);

}
