package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.time.LocalDate;

public class HearingDateUtil {

    private HearingDateUtil(){}

    public static LocalDate stringToDate(final String dateAsString){

        return parse(dateAsString, ISO_LOCAL_DATE);
    }
}
