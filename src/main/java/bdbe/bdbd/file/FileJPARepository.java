package bdbe.bdbd.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileJPARepository extends JpaRepository<File, Long> {
    List<File> findByCarwash_Id(Long carwashId);

    Optional<File> findFirstByCarwashIdOrderByUploadedAtAsc(Long carwashId);
    List<File> findByIsDeletedFalseAndCarwashId(Long carwashId);


}
