package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.Operator.OR;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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

        final QueryBuilder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.getName(), is("nested"));
        assertThat(actualNestedQueryBuilder.query(), instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));


        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));


        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.lastName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.organisationName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is(nameString));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }


    private void assertQueryBuilderForNameStringWithAdditionalBuilders(final String nameString) {

        final QueryBuilder additionalBuilder = new PostCodeQueryBuilder().getQueryBuilderBy("AB1 2CD");
        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(nameString, asList(additionalBuilder));

        final QueryBuilder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.getName(), is("nested"));
        assertThat(actualNestedQueryBuilder.query(), instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));


        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);

        assertThat(shouldQueryBuilder1.must(), hasSize(3));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(2), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(2);
        assertThat(matchQueryBuilder.fieldName(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(3));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(2);
        assertThat(matchQueryBuilder.fieldName(), is("parties.organisationName"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));


        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(1);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.lastName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.organisationName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is(nameString));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder4.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.lastName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is(nameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }


    private void assertQueryBuilderForAliasNameString(final String aliasNameString) {

        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(aliasNameString, null);

        final QueryBuilder actualQueryBuilder = lastOrOrganisationNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder topLevelNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(topLevelNestedQueryBuilder.getName(), is("nested"));
        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));

        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));
        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) topLevelNestedQueryBuilder.query();

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(4));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.lastName"));
        assertThat(matchQueryBuilder.value(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.organisationName"));
        assertThat(matchQueryBuilder.value(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.lastName"));
        assertThat(matchQueryBuilder.value(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.organisationName"));
        assertThat(matchQueryBuilder.value(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));


        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.lastName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.organisationName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is(aliasNameString));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.lastName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is(aliasNameString));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));


    }

    private void assertPostCodeQueryBuilder(final MatchQueryBuilder matchQueryBuilder) {
        assertThat(matchQueryBuilder.fieldName(), is("parties.postCode"));
        assertThat(matchQueryBuilder.value(), is("AB1 2CD"));
        assertThat(matchQueryBuilder.operator(), is(AND));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));
    }

}