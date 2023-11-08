package bdbe.bdbd.dto.review;

import bdbe.bdbd._core.utils.DateUtils;
import bdbe.bdbd.model.keyword.Keyword;
import bdbe.bdbd.model.review.Review;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.model.keyword.reviewKeyword.ReviewKeyword;
import bdbe.bdbd.model.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponse {

    @Getter
    @Setter
    public static class getReviewById{
        private Long id;
        private Long uId;
        private Long cId;
        private Reservation reservation; //1:1, 참조용(read-only)
        private String comment;
        private double rate;

        public getReviewById(Review review) {
            this.id = review.getId();
            this.uId = review.getMember().getId();
            this.cId = review.getCarwash().getId();
            this.reservation = review.getReservation();
            this.comment = review.getComment();
            this.rate = review.getRate();
        }

    }
    @Getter
    @Setter
    public static class ReviewByCarwashIdDTO {
        private double rate;
        private String username;
        private String created_at;
        private String comment;
        private List<Long> keywordIdList;

        public ReviewByCarwashIdDTO(Review review, Member member, List<ReviewKeyword> reviewKeyword) {
            this.rate = review.getRate();
            this.username = member.getUsername();
            this.created_at = DateUtils.formatDateTime(review.getCreatedAt());
            this.comment = review.getComment();
            this.keywordIdList = reviewKeyword.stream()
                    .map(rk -> rk.getKeyword().getId())
                    .collect(Collectors.toList());
        }
    }
    @Getter
    @Setter
    public static class ReviewResponseDTO {
        private ReviewOverviewDTO overview;
        private List<ReviewByCarwashIdDTO> reviews;

        public ReviewResponseDTO(ReviewOverviewDTO overview, List<ReviewByCarwashIdDTO> reviews) {
            this.overview = overview;
            this.reviews = reviews;
        }
    }
    @Getter
    @Setter
    @ToString
    public static class ReviewOverviewDTO {
        private double rate; // 세차장 별점
        private int totalCnt; // 리뷰 총 갯수

        private List<ReviewKeywordCnt> reviewKeyword;
    }
    @Getter
    @Setter
    @ToString
    public static class ReviewKeywordCnt {
        private Long id;
        private int count;

        public ReviewKeywordCnt(Long keywordId, int cnt) {
            this.id = keywordId;
            this.count = cnt;
        }
    }

    @Getter
    @Setter
    public static class ReviewKeywordResponseDTO {
        private List<ReviewKeywordDTO> reviewKeyword;

        public ReviewKeywordResponseDTO(List<Keyword> keywordList) {
            this.reviewKeyword = keywordList.stream()
                    .map(ReviewKeywordDTO::new)
                    .collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class ReviewKeywordDTO {
            private Long id;
            private String keyword;

            public ReviewKeywordDTO(Keyword keyword) {
                this.id = keyword.getId();
                this.keyword = keyword.getName();
            }
        }
    }

}


