package bdbe.bdbd.dto.review;

import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.model.review.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
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

        private List<Long> keywordIdList;

        @NotNull(message = "Rate is required")
        @Min(value = 1, message = "The rating must be at least 1 point")
        @Max(value = 5, message = "The rating cannot exceed 5 points")
        private Integer rate;

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
