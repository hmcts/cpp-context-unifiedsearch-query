package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME_PATH_NGRAMMED;

import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class OperationNameQueryBuilderTest {

    private OperationNameQueryBuilder operationNameQueryBuilder = new OperationNameQueryBuilder();

    @Test
    public void shouldReturnOperationNameQueryBuilder() {
        final String operationName = "Shield Service";
        final Query query = operationNameQueryBuilder.getQueryBuilderBy(operationName);

        assertThat(query, notNullValue());
        assertThat(query.bool(), notNullValue());

        final BoolQuery booleanQueryBuilder = query.bool();
        assertThat(booleanQueryBuilder.must(), hasSize(0));
        assertThat(booleanQueryBuilder.should(), hasSize(2));

        final Query operationShould = booleanQueryBuilder.should().get(0);
        final Query operationNgrammedShoud = booleanQueryBuilder.should().get(1);
        assertThat(operationShould.match(), notNullValue());
        assertThat(operationNgrammedShoud.match(), notNullValue());

        final MatchQuery operationQueryBuilder = operationShould.match();
        assertThat(operationQueryBuilder.query().stringValue(), is(operationName));
        assertThat(operationQueryBuilder.field(), is(OPERATION_NAME));

        final MatchQuery operationNgramQueryBuilder = operationNgrammedShoud.match();
        assertThat(operationNgramQueryBuilder.query().stringValue(), is(operationName));
        assertThat(operationNgramQueryBuilder.field(), is(OPERATION_NAME_PATH_NGRAMMED));
    }

}