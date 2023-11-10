package bdbe.bdbd.repository.keyword.carwashKeyword;

import bdbe.bdbd.model.keyword.Keyword;
import bdbe.bdbd.model.keyword.carwashKeyword.CarwashKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarwashKeywordJPARepository extends JpaRepository<CarwashKeyword, Long> {
    List<CarwashKeyword> findByKeywordIn(List<Keyword> keywords);

    @Query("SELECT ck.keyword.id FROM CarwashKeyword ck WHERE ck.carwash.id = :carwashId")
    List<Long> findKeywordIdsByCarwashId(@Param("carwashId") Long carwashId);

    @Query("DELETE FROM CarwashKeyword ck WHERE ck.carwash.id = :carwashId AND ck.keyword.id IN :keywordIds")
    @Modifying
    void deleteByCarwashIdAndKeywordIds(@Param("carwashId") Long carwashId, @Param("keywordIds") List<Long> keywordIds);
}
