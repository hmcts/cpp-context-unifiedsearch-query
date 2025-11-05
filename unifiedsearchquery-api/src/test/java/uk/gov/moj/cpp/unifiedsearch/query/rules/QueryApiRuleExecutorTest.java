package uk.gov.moj.cpp.unifiedsearch.query.rules;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.unifiedsearch.query.api.accesscontrol.PermissionConstants.getPermissionsForViewCPSSearch;
import static uk.gov.moj.cpp.unifiedsearch.query.api.accesscontrol.PermissionConstants.getPermissionsForViewCPSSearchRestricted;

import uk.gov.moj.cpp.accesscontrol.common.providers.UserAndGroupProvider;
import uk.gov.moj.cpp.accesscontrol.drools.Action;
import uk.gov.moj.cpp.accesscontrol.test.utils.BaseDroolsAccessControlTest;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.ExecutionResults;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QueryApiRuleExecutorTest extends BaseDroolsAccessControlTest {

    public static final String QUERY_CPS_CASE = "unifiedsearch.query.cps.cases";
    public static final String QUERY_WITNESS_CARE_UNIT_CASE = "unifiedsearch.query.cps-wcu.cases";
    protected Action action;

    @Mock
    protected UserAndGroupProvider userAndGroupProvider;



    public QueryApiRuleExecutorTest() {
        super("QUERY_API_SESSION");
    }

    @Override
    protected Map<Class<?>, Object> getProviderMocks() {
        return Collections.singletonMap(UserAndGroupProvider.class, userAndGroupProvider);
    }


    @Test
    public void shouldAllowCPSUserToSearchCaseWhenTheyHaveTheCorrectPermission() throws JsonProcessingException {
            action = createActionFor(QUERY_CPS_CASE);
            when(userAndGroupProvider.hasPermission(action, getPermissionsForViewCPSSearch())).thenReturn(true);
            final ExecutionResults executionResults = executeRulesWith(action);
            assertSuccessfulOutcome(executionResults);
            verify(userAndGroupProvider).hasPermission(action, getPermissionsForViewCPSSearch());
            verifyNoMoreInteractions(userAndGroupProvider);

    }


    @Test
    public void shouldNotAllowCPSUserToSearchCaseWhenTheyDoNotHaveTheCorrectPermission() throws JsonProcessingException {
            action = createActionFor(QUERY_CPS_CASE);
            when(userAndGroupProvider.hasPermission(action, getPermissionsForViewCPSSearch())).thenReturn(false);
            final ExecutionResults executionResults = executeRulesWith(action);
            assertFailureOutcome(executionResults);
            verify(userAndGroupProvider).hasPermission(action, getPermissionsForViewCPSSearch());
            verifyNoMoreInteractions(userAndGroupProvider);

    }

    @Test
    public void shouldAllowWitnessCareUserToSearchCaseWhenTheyHaveTheCorrectPermission() throws JsonProcessingException {
        action = createActionFor(QUERY_WITNESS_CARE_UNIT_CASE);
        when(userAndGroupProvider.hasPermission(action, getPermissionsForViewCPSSearchRestricted())).thenReturn(true);
        final ExecutionResults executionResults = executeRulesWith(action);
        assertSuccessfulOutcome(executionResults);
        verify(userAndGroupProvider).hasPermission(action, getPermissionsForViewCPSSearchRestricted());
        verifyNoMoreInteractions(userAndGroupProvider);

    }


    @Test
    public void shouldNotAllowWitnessCareUserToSearchCaseWhenTheyDoNotHaveTheCorrectPermission() throws JsonProcessingException {
        action = createActionFor(QUERY_WITNESS_CARE_UNIT_CASE);
        when(userAndGroupProvider.hasPermission(action, getPermissionsForViewCPSSearchRestricted())).thenReturn(false);
        final ExecutionResults executionResults = executeRulesWith(action);
        assertFailureOutcome(executionResults);
        verify(userAndGroupProvider).hasPermission(action, getPermissionsForViewCPSSearchRestricted());
        verifyNoMoreInteractions(userAndGroupProvider);

    }


}
