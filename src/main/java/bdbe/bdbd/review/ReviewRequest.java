package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class ReviewRequest {
    @Getter
    @Setter
    @ToString
    public static class SaveDTO {
        private Long carwashId;
        private Long reservationId;
        private List<Long> keywordIdList;
        private double rate;
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
