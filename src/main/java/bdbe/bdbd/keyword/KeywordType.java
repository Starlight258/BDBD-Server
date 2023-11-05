package bdbe.bdbd.keyword;

import bdbe.bdbd._core.errors.exception.NotFoundError;
import lombok.Getter;
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
        throw new NotFoundError("Unknown enum value: " + value);
    }
}
