package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;


import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME_PATH_NGRAMMED;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class OperationNameQueryBuilderTest {

    private OperationNameQueryBuilder operationNameQueryBuilder = new OperationNameQueryBuilder();

    @Test
    public void shouldReturnOperationNameQueryBuilder() {
        final String operationName = "Shield Service";
        final QueryBuilder queryBuilder = operationNameQueryBuilder.getQueryBuilderBy(operationName);

        assertThat(queryBuilder, notNullValue());
        assertThat(queryBuilder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder booleanQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(booleanQueryBuilder.must(), hasSize(0));
        assertThat(booleanQueryBuilder.should(), hasSize(2));

        final QueryBuilder operationShould = booleanQueryBuilder.should().get(0);
        final QueryBuilder operationNgrammedShoud = booleanQueryBuilder.should().get(1);
        assertThat(operationShould, instanceOf(MatchQueryBuilder.class));
        assertThat(operationNgrammedShoud, instanceOf(MatchQueryBuilder.class));

        final MatchQueryBuilder operationQueryBuilder = (MatchQueryBuilder) operationShould;
        assertThat(operationQueryBuilder.getName(), is("match"));
        assertThat(operationQueryBuilder.value(), is(operationName));
        assertThat(operationQueryBuilder.fieldName(), is(OPERATION_NAME));

        final MatchQueryBuilder operationNgramQueryBuilder = (MatchQueryBuilder) operationNgrammedShoud;
        assertThat(operationNgramQueryBuilder.getName(), is("match"));
        assertThat(operationNgramQueryBuilder.value(), is(operationName));
        assertThat(operationNgramQueryBuilder.fieldName(), is(OPERATION_NAME_PATH_NGRAMMED));
    }

}