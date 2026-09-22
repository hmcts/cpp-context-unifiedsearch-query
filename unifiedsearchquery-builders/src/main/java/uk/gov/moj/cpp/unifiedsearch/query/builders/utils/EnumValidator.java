package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class EnumValidator {
    private EnumValidator(){}

    public static  <T extends Enum<T>> boolean isValidEnum(Class<T> enumType, String key){
        try {
            final String value = trimToEmpty(key);
            Enum.valueOf(enumType, value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase());
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }
}
