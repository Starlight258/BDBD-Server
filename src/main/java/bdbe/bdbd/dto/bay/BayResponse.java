package bdbe.bdbd.dto.bay;

import bdbe.bdbd.model.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;

public class BayResponse {
    @Getter
    @Setter
    public static class FindAllDTO {

        private Long id;
        private String des;
        private String name;
        private double rate;
        private String tel;
        private Long rId;
        private Long userId;

        public FindAllDTO(Carwash carwash) {
            this.id = carwash.getId();
            this.des = carwash.getDes();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.tel = carwash.getTel();
            this.rId = carwash.getLocation().getId();
            this.userId = carwash.getMember().getId();
        }
    }

    @Getter
    @Setter
    public class KeywordResponseDTO {
        private String keywordName;

        public KeywordResponseDTO(String keywordName) {
            this.keywordName = keywordName;
        }
    }

    @Getter
    @Setter
    public static class BayRevenueResponseDTO {
        private Long revenue;
        private Long reservationCnt;
    }
}
