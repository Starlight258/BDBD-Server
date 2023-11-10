package bdbe.bdbd.dto.bay;

import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.model.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

public class BayRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotNull(message = "Bay number is required")
        private Integer bayNum;

        public Bay toBayEntity(Carwash carwash) {
            return Bay.builder()
                    .carwash(carwash)
                    .bayNum(bayNum)
                    .status(1) // 활성화 상태로 생성
                    .build();
        }
    }
}