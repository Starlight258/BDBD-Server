package bdbe.bdbd.dto.review;

import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.model.review.Review;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.model.member.Member;
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

        @NotNull(message = "CarwashId is required")
        private Long carwashId;

        @NotNull(message = "ReservationId is required")
        private Long reservationId;

        private List<Long> keywordIdList; // 필수가 아님

        @NotNull(message = "Rate is required")
        @DecimalMax(value = "5.0", message = "The rating cannot exceed 5 points")
        private double rate;

        @Size(max = 100, message = "Comments must be less than 100 characters")
        @NotBlank(message = "Comments is required")
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
