package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InactiveCaseStatusEnum {
  INACTIVE,
  CLOSED,
  REFERRED_FOR_COURT_HEARING,
  COMPLETED,
  REOPENED_IN_LIBRA,
  EJECTED;

  public static List<String> getInactiveCaseStatusEnumValues() {
    return Stream.of(values()).map(Enum::name).collect(Collectors.toList());
  }
}
