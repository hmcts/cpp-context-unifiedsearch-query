package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ActiveCaseStatusEnum {
  INCOMPLETE("INCOMPLETE"),
  READY_FOR_REVIEW("READY_FOR_REVIEW"),
  SJP_REFERRAL("SJP_REFERRAL"),
  ACTIVE("ACTIVE"),
  NO_PLEA_RECEIVED("NO_PLEA_RECEIVED"),
  AOCP_PENDING("AOCP_PENDING"),
  NO_PLEA_RECEIVED_READY_FOR_DECISION("NO_PLEA_RECEIVED_READY_FOR_DECISION"),
  PLEA_RECEIVED_READY_FOR_DECISION("PLEA_RECEIVED_READY_FOR_DECISION"),
  PLEA_RECEIVED_NOT_READY_FOR_DECISION("PLEA_RECEIVED_NOT_READY_FOR_DECISION"),
  SET_ASIDE_READY_FOR_DECISION("SET_ASIDE_READY_FOR_DECISION"),
  WITHDRAWAL_REQUEST_READY_FOR_DECISION("WITHDRAWAL_REQUEST_READY_FOR_DECISION"),
  COMPLETED_APPLICATION_PENDING("COMPLETED_APPLICATION_PENDING"),
  RELISTED("RELISTED"),
  APPEALED("APPEALED"),
  UNKNOWN("UNKNOWN"),
  NULL("NULL"),
  EMPTY("");

  private String description;

  private ActiveCaseStatusEnum(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static List<String> getActiveCaseStatusEnumValues() {
    return Stream.of(values()).map(Enum::name).collect(Collectors.toList());
  }
}