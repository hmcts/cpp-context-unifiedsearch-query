package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class EnumValidator {
    private EnumValidator(){}

    public static  <T extends Enum<T>> boolean isValidEnum(Class<T> enumType, String key){
        try {
            Enum.valueOf(enumType, trimToEmpty(key).toUpperCase());
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }
}
