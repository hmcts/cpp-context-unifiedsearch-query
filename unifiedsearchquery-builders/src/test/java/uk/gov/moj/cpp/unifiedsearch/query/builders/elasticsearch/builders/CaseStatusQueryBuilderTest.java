package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.ActiveCaseStatusEnum.getActiveCaseStatusEnumValues;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CaseStatusQueryBuilderTest {

    private CaseStatusQueryBuilder caseStatusQueryBuilder;

    @BeforeEach
    public void setUp() {
        this.caseStatusQueryBuilder = new CaseStatusQueryBuilder();
    }


    @Test
    public void shouldGetQueryBuilderBy() {

        final QueryBuilder actualQueryBuilder = caseStatusQueryBuilder.getQueryBuilderBy(" ACTIVE  , INACTIVE  ");

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermsQueryBuilder.class));

        final TermsQueryBuilder actualTermsQuery = (TermsQueryBuilder) actualQueryBuilder;

        assertThat(actualTermsQuery.fieldName(), is("caseStatus"));
        final List<Object> queryValues = actualTermsQuery.values();

        assertThat(queryValues, hasSize(2));
        assertThat(queryValues, hasItems("ACTIVE","INACTIVE"));
    }

    @Test
    public void testCleanWithNull() {
        final String[] result = caseStatusQueryBuilder.clean(null);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("null", result[0]);
    }

    @Test
    public void testCleanWithString() {
        final String[] result = caseStatusQueryBuilder.clean("EJECTED");
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("EJECTED", result[0]);
    }

    @Test
    public void testCleanWithStringArray() {
        final String[] result = caseStatusQueryBuilder.clean("ACTIVE, EJECTED");
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("ACTIVE", result[0]);
        assertEquals("EJECTED", result[1]);
    }

    @Test
    public void testCleanWithEmptyCollection() {
        final String[] result = caseStatusQueryBuilder.clean(emptyList());
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(EMPTY, result[0]);
    }

    @Test
    public void testCleanWithCollection() {
        final String[] result = caseStatusQueryBuilder.clean(getActiveCaseStatusEnumValues());
        assertNotNull(result);
        assertEquals(getActiveCaseStatusEnumValues().size(), result.length);
        assertArrayEquals(getActiveCaseStatusEnumValues().toArray(), result);
    }
}