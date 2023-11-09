package bdbe.bdbd.repository.keyword;

import bdbe.bdbd.model.Code.KeywordType;
import bdbe.bdbd.model.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordJPARepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByType(KeywordType type);
}
