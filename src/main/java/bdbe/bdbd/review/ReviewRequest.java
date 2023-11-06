package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ReviewRequest {
    @Getter
    @Setter
    @ToString
    public static class SaveDTO {
        @NotNull(message = "세차장 ID는 필수입니다.")
        private Long carwashId;

        @NotNull(message = "예약 ID는 필수입니다.")
        private Long reservationId;

        private List<Long> keywordIdList; // 필수가 아님

        @NotNull(message = "평점은 필수입니다.")
        @DecimalMax(value = "5.0", message = "평점은 5점을 초과할 수 없습니다.")
        private double rate;

        @Size(max = 100, message = "댓글은 100자 이내여야 합니다.")
        @NotBlank(message = "댓글은 필수입니다.")
        private String comment;


        public Review toReviewEntity(Member member, Carwash carwash, Reservation reservation) {
            return Review.builder()
                    .member(member)
                    .carwash(carwash)
                    .reservation(reservation)
                    .comment(comment)
                    .rate(rate)
                    .build();
        }
    }
}
