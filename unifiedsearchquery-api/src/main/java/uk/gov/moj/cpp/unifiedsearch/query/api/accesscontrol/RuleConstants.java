package uk.gov.moj.cpp.unifiedsearch.query.api.accesscontrol;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public final class RuleConstants {

    private static final String GROUP_LEGAL_ADVISERS = "Legal Advisers";
    private static final String GROUP_COURT_ADMINISTRATORS = "Court Administrators";
    private static final String GROUP_CLERKS = "Court Clerks";
    private static final String GROUP_LISTING_OFFICERS = "Listing Officers";
    private static final String GROUP_CROWN_COURT_ADMIN = "Crown Court Admin";
    private static final String GROUP_SYSTEM_USERS = "System Users";

    //To be fully implemented in GPE-12284
    private static final List<String> LAA_USER_GROUPS = asList(GROUP_SYSTEM_USERS);
    private static final List<String> PROBATION_DEFENDANT_DETAILS_USER_GROUPS = asList(GROUP_SYSTEM_USERS,"Probation Caseworker");

    private static final List<String> DEFENDANT_SEARCH_USER_GROUPS = asList(GROUP_SYSTEM_USERS);
    public static final String YOUTH_OFFENDING_SERVICE_ADMIN = "Youth Offending Service Admin";
    private static final String GROUP_COURT_ASSOCIATE = "Court Associate";
    public static final String GROUP_DEPUTIES = "Deputies";
    public static final String GROUP_DJMC = "DJMC";
    public static final String GROUP_JUDGE = "Judge";
    private static final String GROUP_NCES = "NCES";
    private static final String GROUP_RECORDERS = "Recorders";
    private static final String GROUP_CPS = "CPS";
    private static final String GROUP_POLICE_ADMIN = "Police Admin";
    private static final String GROUP_VICTIMS_WITNESS_CARE_ADMIN = "Victims & Witness Care Admin";
    private static final String GROUP_PROBATION_ADMIN = "Probation Admin";
    private static final String PROSECUTOR = "Prosecutor";
    private static final String OPERATIONAL_DELIVERY ="Operational Delivery";
    private static final String PARALEGAL = "Paralegal";
    private static final String PROSECUTION_ADVOCATE = "Prosecution Advocate";
    private static final String MANAGER = "Manager";
    private static final String SENIOR_MANAGER = "Senior Manager";
    private static final String EXTERNAL_ADVOCATE = "External Advocate";
    private static final String UNIT_CASE_ACCESS_MANAGER = "Unit Case Access Manager";
    private static final String WITNESS_CARE_OFFICER = "Witness Care Officer";
    private static final String WITNESS_CARE_MANAGER = "Witness Care Manager";

    private static final String SECOND_LINE_SUPPORT = "Second Line Support";
    private RuleConstants() {
    }

    public static List<String> getUnifiedSearchGroups() {
        return asList(
                GROUP_LEGAL_ADVISERS,
                GROUP_COURT_ADMINISTRATORS,
                GROUP_CROWN_COURT_ADMIN,
                GROUP_CLERKS,
                GROUP_LISTING_OFFICERS,
                YOUTH_OFFENDING_SERVICE_ADMIN,
                GROUP_COURT_ASSOCIATE,
                GROUP_DEPUTIES,
                GROUP_DJMC,
                GROUP_JUDGE,
                GROUP_NCES,
                GROUP_RECORDERS,
                GROUP_CPS,
                GROUP_POLICE_ADMIN,
                GROUP_VICTIMS_WITNESS_CARE_ADMIN,
                GROUP_PROBATION_ADMIN,
                GROUP_SYSTEM_USERS,
                SECOND_LINE_SUPPORT
        );
    }

    public static List<String> getUnifiedSearchGroupsForLAA() {
        return unmodifiableList(LAA_USER_GROUPS);
    }

    public static List<String> getUnifiedSearchGroupsForProbationDefendantDetails() {
        return unmodifiableList(PROBATION_DEFENDANT_DETAILS_USER_GROUPS);
    }

    public static List<String> getUnifiedSearchGroupsForDefendantSearch() {
        return unmodifiableList(DEFENDANT_SEARCH_USER_GROUPS);
    }

    public static List<String> getUnifiedSearchGroupsForCPSSearch() {
        return asList(
                PROSECUTOR,
                OPERATIONAL_DELIVERY,
                PARALEGAL,
                PROSECUTION_ADVOCATE,
                MANAGER,
                SENIOR_MANAGER,
                EXTERNAL_ADVOCATE,
                UNIT_CASE_ACCESS_MANAGER
        );
    }

    public static List<String> getUnifiedSearchGroupsForWCUSearch() {
        return asList(
                WITNESS_CARE_OFFICER,
                WITNESS_CARE_MANAGER
        );
    }

}
