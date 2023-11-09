package bdbe.bdbd.repository.location;

import bdbe.bdbd.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJPARepository extends JpaRepository<Location, Long> {
}
