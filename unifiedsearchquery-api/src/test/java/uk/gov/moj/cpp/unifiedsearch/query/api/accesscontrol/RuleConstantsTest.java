package uk.gov.moj.cpp.unifiedsearch.query.api.accesscontrol;


import static com.google.common.collect.ImmutableList.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import uk.gov.moj.cpp.accesscontrol.common.providers.UserAndGroupProvider;
import uk.gov.moj.cpp.accesscontrol.drools.Action;
import uk.gov.moj.cpp.accesscontrol.test.utils.BaseDroolsAccessControlTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.runtime.ExecutionResults;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RuleConstantsTest extends BaseDroolsAccessControlTest {

    @Mock
    private UserAndGroupProvider userAndGroupProvider;

    private static final List USERS_GROUPS_EXPECTED = List.of(
            "Legal Advisers",
            "Court Administrators",
            "Crown Court Admin",
            "Court Clerks",
            "Listing Officers",
            "Youth Offending Service Admin",
            "Court Associate",
            "Deputies",
            "DJMC",
            "Judge",
            "NCES",
            "Recorders",
            "CPS",
            "Police Admin",
            "Victims & Witness Care Admin",
            "Probation Admin",
            "System Users",
            "Second Line Support");

    private static final List<String> USERS_GROUPS_EXPECTED_FOR_LAA = of("System Users");
    private static final List<String> USERS_GROUPS_EXPECTED_FOR_PROBATION_DEFENDANT_DETAILS = of("System Users", "Probation Caseworker");
    private static final List<String> USERS_GROUPS_EXPECTED_FOR_DEFENDANT_SEARCH = of("System Users");
    private static final List<String> USER_GROUPS_EXPECTED_FOR_CPS = of("Prosecutor", "Operational Delivery", "Paralegal", "Prosecution Advocate", "Manager", "Senior Manager", "External Advocate", "Unit Case Access Manager");
    private static final List<String> USER_GROUPS_EXPECTED_FOR_WCU = of("Witness Care Officer", "Witness Care Manager");


    private static final String UNIFIEDSEARCH_QUERY_CASES = "unifiedsearch.query.cases";

    public static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(UNIFIEDSEARCH_QUERY_CASES, (RuleConstantsToTest) RuleConstants::getUnifiedSearchGroups)
        );
    }

    public RuleConstantsTest() {
        super("QUERY_API_SESSION");
    }

    @Test
    public void shouldReturnAllUserGroups() {

        final List<String> userGroups = RuleConstants.getUnifiedSearchGroups();
        assertEquals(USERS_GROUPS_EXPECTED, userGroups);

    }

    @Test
    public void shouldReturnAllUserGroupsForLaa() {
        final List<String> userGroups = RuleConstants.getUnifiedSearchGroupsForLAA();
        assertThat(userGroups, is(USERS_GROUPS_EXPECTED_FOR_LAA));
    }

    @Test
    public void shouldReturnAllUserGroupsForProbationDefendantDetails() {
        final List<String> userGroups = RuleConstants.getUnifiedSearchGroupsForProbationDefendantDetails();
        assertThat(userGroups, is(USERS_GROUPS_EXPECTED_FOR_PROBATION_DEFENDANT_DETAILS));
    }


    @Test
    public void shouldReturnAllUserGroupsForDefendantSearch() {
        final List<String> userGroups = RuleConstants.getUnifiedSearchGroupsForDefendantSearch();
        assertThat(userGroups, is(USERS_GROUPS_EXPECTED_FOR_DEFENDANT_SEARCH));
    }

    @Test
    public void shouldReturnAllUserGroupsForCPSSearch() {
        final List<String> userGroups = RuleConstants.getUnifiedSearchGroupsForCPSSearch();
        assertThat(userGroups, is(USER_GROUPS_EXPECTED_FOR_CPS));
    }

    @Test
    public void shouldReturnAllUserGroupsForWCUSearch() {
        final List<String> userGroups = RuleConstants.getUnifiedSearchGroupsForWCUSearch();
        assertThat(userGroups, is(USER_GROUPS_EXPECTED_FOR_WCU));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void shouldAllowAuthorisedAction(final String actionName, final RuleConstantsToTest ruleConstantsToTest) {
        verifyAuthorisedAccessQuery(actionName, ruleConstantsToTest.getActionGroups());
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void shouldNotAllowUnauthorisedAction(final String actionName, final RuleConstantsToTest ruleConstantsToTest) {
        final Action action = createActionFor(actionName);
        given(userAndGroupProvider.isMemberOfAnyOfTheSuppliedGroups(action, ruleConstantsToTest.getActionGroups()))
                .willReturn(false);

        final ExecutionResults results = executeRulesWith(action);
        assertFailureOutcome(results);
    }

    private void verifyAuthorisedAccessQuery(final String name, final List<String> groupNames) {
        final Action action = createActionFor(name);
        given(userAndGroupProvider.isMemberOfAnyOfTheSuppliedGroups(action, groupNames))
                .willReturn(true);
        final ExecutionResults results = executeRulesWith(action);
        assertSuccessfulOutcome(results);
    }

    @Override
    protected Map<Class<?>, Object> getProviderMocks() {
        return Collections.singletonMap(UserAndGroupProvider.class, userAndGroupProvider);
    }

    @FunctionalInterface
    private interface RuleConstantsToTest {
        List<String> getActionGroups();
    }
}
