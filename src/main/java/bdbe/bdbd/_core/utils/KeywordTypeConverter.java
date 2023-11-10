package bdbe.bdbd._core.utils;


import bdbe.bdbd.model.Code.KeywordType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA에서 KeywordType 열거형과 정수 데이터 타입 간의 변환을 수행
 * 데이터베이스와 엔티티 간의 변환 로직 포함
 */
@Converter(autoApply = true)
public class KeywordTypeConverter implements AttributeConverter<KeywordType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(KeywordType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public KeywordType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : KeywordType.fromValue(dbData);
    }
}


