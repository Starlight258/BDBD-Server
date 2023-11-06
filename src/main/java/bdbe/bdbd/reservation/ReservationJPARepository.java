package bdbe.bdbd.reservation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
//    Reservation findFirstByIsDeletedFalse();  // 테스트시에 사용

    @Query("SELECT r FROM Reservation r JOIN FETCH r.bay b JOIN FETCH b.carwash WHERE r.member.id = :memberId AND r.isDeleted = false")
    List<Reservation> findFirstByMemberIdWithJoinFetch(@Param("memberId") Long memberId, Pageable pageable);

    List<Reservation> findByBayIdInAndIsDeletedFalse(List<Long> bayIds); // bay id 리스트로 관련된 모든 reservation 찾기

    @Query("select r from Reservation r " +
            "join fetch r.member m " +
            "join fetch r.bay b " +
            "join fetch b.carwash c " +
            "where b.id = :bayId and r.isDeleted = false")
    List<Reservation> findByBay_IdWithJoinsAndIsDeletedFalse(@Param("bayId") Long bayId);

    List<Reservation> findByBay_IdAndIsDeletedFalse(Long bayId); // 베이의 예약 목록 찾기

    List<Reservation> findByMemberIdAndIsDeletedFalse(Long memberId); // member의 예약 목록 찾기

    @Query("SELECT r FROM Reservation r JOIN FETCH r.bay b JOIN FETCH b.carwash WHERE r.member.id = :memberId AND r.isDeleted = false")
    List<Reservation> findByMemberIdJoinFetch(@Param("memberId") Long memberId, Pageable pageable);

    // 이번 달 예약 가져오기
    @Query("SELECT r FROM Reservation r JOIN FETCH r.bay b JOIN FETCH b.carwash c JOIN FETCH r.member u WHERE c.id IN :carwashIds AND FUNCTION('YEAR', r.startTime) = FUNCTION('YEAR', :selectedDate) AND FUNCTION('MONTH', r.startTime) = FUNCTION('MONTH', :selectedDate) AND r.isDeleted = false ORDER BY r.startTime DESC")
    List<Reservation> findAllByCarwash_IdInOrderByStartTimeDesc(@Param("carwashIds") List<Long> carwashIds, @Param("selectedDate") LocalDate selectedDate);

    // 오늘 날짜 예약 가져오기
    @Query("SELECT r FROM Reservation r JOIN FETCH r.bay b JOIN FETCH b.carwash c WHERE c.id = :carwashId AND FUNCTION('DATE', r.startTime) = :today AND r.isDeleted = false ORDER BY r.startTime DESC")
    List<Reservation> findTodaysReservationsByCarwashId(@Param("carwashId") Long carwashId, @Param("today") Date today);

    // 세차장 id들로 예약 수 구하기
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bay.carwash.id IN :carwashIds AND FUNCTION('YEAR', r.startTime) = FUNCTION('YEAR', :selectedDate) AND FUNCTION('MONTH', r.startTime) = FUNCTION('MONTH', :selectedDate) AND r.isDeleted = false")
    Long findMonthlyReservationCountByCarwashIdsAndDate(@Param("carwashIds") List<Long> carwashIds, @Param("selectedDate") LocalDate selectedDate);

    // 하나의 세차장 id로 예약 수 구하기
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bay.carwash.id = :carwashId AND FUNCTION('YEAR', r.startTime) = FUNCTION('YEAR', :selectedDate) AND FUNCTION('MONTH', r.startTime) = FUNCTION('MONTH', :selectedDate) AND r.isDeleted = false")
    Long findMonthlyReservationCountByCarwashIdAndDate(@Param("carwashId") Long carwashId, @Param("selectedDate") LocalDate selectedDate);

    // 세차장 id들로 판매 수익 구하기
    @Query("SELECT COALESCE(SUM(r.price), 0) FROM Reservation r WHERE r.bay.carwash.id IN :carwashIds AND FUNCTION('YEAR', r.startTime) = FUNCTION('YEAR', :selectedDate) AND FUNCTION('MONTH', r.startTime) = FUNCTION('MONTH', :selectedDate) AND r.isDeleted = false")
    Long findTotalRevenueByCarwashIdsAndDate(@Param("carwashIds") List<Long> carwashIds, @Param("selectedDate") LocalDate selectedDate);


    // 하나의 세차장 id로 판매 수익 구하기
    @Query("SELECT COALESCE(SUM(r.price), 0) FROM Reservation r WHERE r.bay.carwash.id = :carwashId AND FUNCTION('YEAR', r.startTime) = FUNCTION('YEAR', :selectedDate) AND FUNCTION('MONTH', r.startTime) = FUNCTION('MONTH', :selectedDate) AND r.isDeleted = false")
    Long findTotalRevenueByCarwashIdAndDate(@Param("carwashId") Long carwashId, @Param("selectedDate") LocalDate selectedDate);
}
