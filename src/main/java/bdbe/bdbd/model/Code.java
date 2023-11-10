package bdbe.bdbd.model;

import bdbe.bdbd._core.exception.BadRequestError;
import lombok.Getter;

import java.util.Collections;

public class Code {
    @Getter
    public enum DayType {
        WEEKDAY("평일"),
        WEEKEND("주말"),
        HOLIDAY("휴일");

        private final String dayName;

        DayType(String dayName) {
            this.dayName = dayName;
        }
    }

    @Getter
    public enum KeywordType {
        CARWASH(1),
        REVIEW(2);

        private final int value;

        KeywordType(int value) {
            this.value = value;
        }

        public static KeywordType fromValue(int value) {
            for (KeywordType type : KeywordType.values()) {
                if (type.getValue() == value) {

                    return type;
                }
            }
            throw new BadRequestError(
                    BadRequestError.ErrorCode.WRONG_REQUEST_TRANSMISSION,
                    Collections.singletonMap("Value", "Unknown enum value" + value)
            );
        }
    }

    public enum MemberRole {
        ROLE_USER, ROLE_OWNER, ROLE_ADMIN
    }
}
