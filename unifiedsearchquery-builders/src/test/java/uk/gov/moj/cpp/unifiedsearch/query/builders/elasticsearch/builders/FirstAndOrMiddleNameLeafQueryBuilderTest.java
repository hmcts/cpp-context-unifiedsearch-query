package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.Or;
import static co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.CrossFields;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class FirstAndOrMiddleNameLeafQueryBuilderTest {


    @Test
    public void shouldCreateValidBuilderForFirstName() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John", null);

        final Query.Builder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery actualNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(actualNestedQueryBuilder, notNullValue());


        assertThat(actualNestedQueryBuilder.query().bool(), notNullValue());

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();
        final BoolQuery shouldQueryBuilder5 = boolQueryBuilder.should().get(4).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(1));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(1));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields().stream().anyMatch( v -> v.startsWith("parties.middleName")), is(false));
        assertThat(multiMatchQueryBuilder.query(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(And));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = shouldQueryBuilder4.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields().stream().anyMatch( v -> v.startsWith("parties.middleName")), is(false));
        assertThat(multiMatchQueryBuilder.query(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder5.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }


    @Test
    public void shouldCreateValidBuilderForFirstNameAlias() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John", null);

        final Query.Builder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery topLevelNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(topLevelNestedQueryBuilder, notNullValue());


        assertThat(topLevelNestedQueryBuilder.query().nested(), notNullValue());

        assertThat(topLevelNestedQueryBuilder.query().nested(), notNullValue());
        final NestedQuery actualNestedQueryBuilder = topLevelNestedQueryBuilder.query().nested();

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(5));


        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();
        final BoolQuery shouldQueryBuilder5 = boolQueryBuilder.should().get(4).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(1));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(1));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields().stream().anyMatch( v -> v.startsWith("parties.aliases.middleName")), is(false));
        assertThat(multiMatchQueryBuilder.query(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(And));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = shouldQueryBuilder4.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields().stream().anyMatch( v -> v.startsWith("parties.aliases.middleName")), is(false));
        assertThat(multiMatchQueryBuilder.query(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder5.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }

    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleName() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", null);

        final Query.Builder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery actualNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(actualNestedQueryBuilder, notNullValue());

        
        assertThat(actualNestedQueryBuilder.query().bool(), notNullValue());

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();
        final BoolQuery shouldQueryBuilder5 = boolQueryBuilder.should().get(4).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(And));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = shouldQueryBuilder4.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder5.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }


    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleNameInAlias() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", null);

        final Query.Builder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery topLevelNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(topLevelNestedQueryBuilder, notNullValue());

        assertThat(topLevelNestedQueryBuilder.query().nested(), notNullValue());

        assertThat(topLevelNestedQueryBuilder.query().nested(), notNullValue());
        final NestedQuery actualNestedQueryBuilder = topLevelNestedQueryBuilder.query().nested();

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();
        final BoolQuery shouldQueryBuilder5 = boolQueryBuilder.should().get(4).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(And));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder =  shouldQueryBuilder4.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder5.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.firstName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }

    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleNameAndAdditionalBuilder() {

        final Query additionalQuery = new PostCodeQueryBuilder().getQueryBuilderBy("AB1 2CD");
        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", asList(additionalQuery));

        final Query.Builder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery actualNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(actualNestedQueryBuilder, notNullValue());

        
        assertThat(actualNestedQueryBuilder.query().bool(), notNullValue());

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();
        final BoolQuery shouldQueryBuilder5 = boolQueryBuilder.should().get(4).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(3));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(2).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(2).match();
        assertThat(matchQueryBuilder.field(), is("parties.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(3));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(2).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);


        matchQueryBuilder = shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder2.must().get(2).match();
        assertThat(matchQueryBuilder.field(), is("parties.middleName"));
        assertThat(matchQueryBuilder.query().stringValue(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder3.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(1).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(And));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder4.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        multiMatchQueryBuilder = shouldQueryBuilder4.must().get(1).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.firstName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.middleName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder5.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = shouldQueryBuilder5.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }


    private void assertPostCodeQueryBuilder(final MatchQuery matchQueryBuilder) {
        assertThat(matchQueryBuilder.field(), is("parties.postCode"));
        assertThat(matchQueryBuilder.query().stringValue(), is("AB1 2CD"));
        assertThat(matchQueryBuilder.operator(), is(And));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }

}