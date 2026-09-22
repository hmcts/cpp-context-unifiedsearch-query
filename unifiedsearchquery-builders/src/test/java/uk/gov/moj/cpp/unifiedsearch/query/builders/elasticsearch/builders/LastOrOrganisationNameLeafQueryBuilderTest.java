package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.Or;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import org.junit.jupiter.api.Test;

public class LastOrOrganisationNameLeafQueryBuilderTest {


    @Test
    public void shouldCreateValidBuilderForSingleWordNameParameter() {
        assertQueryBuilderForNameString("smith");
    }

    @Test
    public void shouldCreateValidBuilderForSingleWordNameAndAdditionalParameter() {
        assertQueryBuilderForNameStringWithAdditionalBuilders("smith");
    }

    @Test
    public void shouldCreateValidBuilderForMultiWordNameParameter() {
        assertQueryBuilderForNameString("Marks & Spencers");
    }

    @Test
    public void shouldCreateValidBuilderForSingleWordAliasNameParemeter() {
        assertQueryBuilderForAliasNameString("Jackson");
    }

    @Test
    public void shouldCreateValidBuilderForMultiWordAliasNameParemeter() {
        assertQueryBuilderForAliasNameString("Spear And Jackson Brothers Limited");
    }


    private void assertQueryBuilderForNameString(final String nameString) {
        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(nameString, null);

        final Query.Builder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery actualNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(actualNestedQueryBuilder, notNullValue());

        assertThat(actualNestedQueryBuilder.query().bool(), notNullValue());

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());


        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder =  shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));


        MultiMatchQuery multiMatchQueryBuilder =  shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.lastName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.organisationName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is(nameString));
        assertThat(multiMatchQueryBuilder.type(), is(TextQueryType.CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder4.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }


    private void assertQueryBuilderForNameStringWithAdditionalBuilders(final String nameString) {

        final Query additionalQuery = new PostCodeQueryBuilder().getQueryBuilderBy("AB1 2CD");
        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(nameString, asList(additionalQuery));

        final Query.Builder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery actualNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(actualNestedQueryBuilder, notNullValue());


        assertThat(actualNestedQueryBuilder.query().bool(), notNullValue());

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder.bool(), notNullValue());


        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(3));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(2).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(2).match();
        assertThat(matchQueryBuilder.field(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(3));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder2.must().get(2).match();
        assertThat(matchQueryBuilder.field(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));


        matchQueryBuilder = shouldQueryBuilder3.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(1).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.lastName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.organisationName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is(nameString));
        assertThat(multiMatchQueryBuilder.type(), is(TextQueryType.CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder3.must().get(0).match();
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = shouldQueryBuilder4.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.lastName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }


    private void assertQueryBuilderForAliasNameString(final String aliasNameString) {

        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(aliasNameString, null);

        final Query.Builder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        final NestedQuery topLevelNestedQueryBuilder = actualQueryBuilder.build().nested();
        assertThat(topLevelNestedQueryBuilder, notNullValue());

        assertThat(topLevelNestedQueryBuilder.query().nested(), notNullValue());

        final NestedQuery actualNestedQueryBuilder = topLevelNestedQueryBuilder.query().nested();

        final BoolQuery boolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final Query builder = boolQueryBuilder.should().get(0);
        assertThat(builder, notNullValue());

        final BoolQuery shouldQueryBuilder1 = boolQueryBuilder.should().get(0).bool();
        final BoolQuery shouldQueryBuilder2 = boolQueryBuilder.should().get(1).bool();
        final BoolQuery shouldQueryBuilder3 = boolQueryBuilder.should().get(2).bool();
        final BoolQuery shouldQueryBuilder4 = boolQueryBuilder.should().get(3).bool();

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder1.must().get(1).match(), notNullValue());

        MatchQuery matchQueryBuilder = shouldQueryBuilder1.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder1.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0).match(), notNullValue());
        assertThat(shouldQueryBuilder2.must().get(1).match(), notNullValue());

        matchQueryBuilder = shouldQueryBuilder2.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.lastName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));

        matchQueryBuilder = shouldQueryBuilder2.must().get(1).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.organisationName"));
        assertThat(matchQueryBuilder.query().stringValue(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(nullValue()));
        assertThat(matchQueryBuilder.boost(), is(nullValue()));
        assertThat(matchQueryBuilder.fuzziness(), is("AUTO"));


        MultiMatchQuery multiMatchQueryBuilder = shouldQueryBuilder3.must().get(0).multiMatch();
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.lastName^2.0f")));
        assertThat(multiMatchQueryBuilder.fields(), is(hasItem("parties.aliases.organisationName^2.0f")));
        assertThat(multiMatchQueryBuilder.query(), is(aliasNameString));
        assertThat(multiMatchQueryBuilder.type(), is(TextQueryType.CrossFields));
        assertThat(multiMatchQueryBuilder.operator(), is(Or));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = shouldQueryBuilder4.must().get(0).match();
        assertThat(matchQueryBuilder.field(), is("parties.aliases.lastName.ngrammed"));
        assertThat(matchQueryBuilder.query().stringValue(), is(aliasNameString));
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