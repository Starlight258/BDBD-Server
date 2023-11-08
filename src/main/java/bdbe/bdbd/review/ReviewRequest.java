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

        @NotNull(message = "carwashId is required.")
        private Long carwashId;

        @NotNull(message = "reservationId is required..")
        private Long reservationId;

        private List<Long> keywordIdList; // 필수가 아님

        @NotNull(message = "rate is required.")
        @DecimalMax(value = "5.0", message = "평점은 5점을 초과할 수 없습니다.")
        private double rate;

        @Size(max = 100, message = "Comments must be less than 100 characters.")
        @NotBlank(message = "Comments is required.")
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
