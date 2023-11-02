package bdbe.bdbd.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileJPARepository extends JpaRepository<File, Long> {
    List<File> findByCarwash_Id(Long carwashId);

}
