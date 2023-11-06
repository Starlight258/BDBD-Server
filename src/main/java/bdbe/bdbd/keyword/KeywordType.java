package bdbe.bdbd.keyword;

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
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
