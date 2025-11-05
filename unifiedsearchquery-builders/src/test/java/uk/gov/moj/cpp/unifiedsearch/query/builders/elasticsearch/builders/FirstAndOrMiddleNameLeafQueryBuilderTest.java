package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.Operator.OR;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class FirstAndOrMiddleNameLeafQueryBuilderTest {


    @Test
    public void shouldCreateValidBuilderForFirstName() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John", null);

        final QueryBuilder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.getName(), is("nested"));
        assertThat(actualNestedQueryBuilder.query(), instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);
        final BoolQueryBuilder shouldQueryBuilder5 = (BoolQueryBuilder) boolQueryBuilder.should().get(4);

        assertThat(shouldQueryBuilder1.must(), hasSize(1));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(1));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(not(hasKey("parties.middleName"))));
        assertThat(multiMatchQueryBuilder.value(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(AND));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(not(hasKey("parties.middleName"))));
        assertThat(multiMatchQueryBuilder.value(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }


    @Test
    public void shouldCreateValidBuilderForFirstNameAlias() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John", null);

        final QueryBuilder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder topLevelNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(topLevelNestedQueryBuilder.getName(), is("nested"));
        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));

        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));
        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) topLevelNestedQueryBuilder.query();

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(5));


        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);
        final BoolQueryBuilder shouldQueryBuilder5 = (BoolQueryBuilder) boolQueryBuilder.should().get(4);

        assertThat(shouldQueryBuilder1.must(), hasSize(1));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(1));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(not(hasKey("parties.aliases.middleName"))));
        assertThat(multiMatchQueryBuilder.value(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(AND));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(not(hasKey("parties.aliases.middleName"))));
        assertThat(multiMatchQueryBuilder.value(), is("John"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }

    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleName() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", null);

        final QueryBuilder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.getName(), is("nested"));
        assertThat(actualNestedQueryBuilder.query(), instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);
        final BoolQueryBuilder shouldQueryBuilder5 = (BoolQueryBuilder) boolQueryBuilder.should().get(4);

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(AND));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }


    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleNameInAlias() {

        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", null);

        final QueryBuilder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder topLevelNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(topLevelNestedQueryBuilder.getName(), is("nested"));
        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));

        assertThat(topLevelNestedQueryBuilder.query(), instanceOf((NestedQueryBuilder.class)));
        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) topLevelNestedQueryBuilder.query();

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);
        final BoolQueryBuilder shouldQueryBuilder5 = (BoolQueryBuilder) boolQueryBuilder.should().get(4);

        assertThat(shouldQueryBuilder1.must(), hasSize(2));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(2));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(AND));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.aliases.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(0);
        assertThat(matchQueryBuilder.fieldName(), is("parties.aliases.firstName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.7F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

    }

    @Test
    public void shouldCreateValidBuilderForFirstOrMiddleNameAndAdditionalBuilder() {

        final QueryBuilder additionalBuilder = new PostCodeQueryBuilder().getQueryBuilderBy("AB1 2CD");
        final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder("John Smith", asList(additionalBuilder));

        final QueryBuilder actualQueryBuilder = firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder();

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(NestedQueryBuilder.class));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.getName(), is("nested"));
        assertThat(actualNestedQueryBuilder.query(), instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(boolQueryBuilder.should(), hasSize(5));

        final QueryBuilder builder = boolQueryBuilder.should().get(0);
        assertThat(builder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder shouldQueryBuilder1 = (BoolQueryBuilder) boolQueryBuilder.should().get(0);
        final BoolQueryBuilder shouldQueryBuilder2 = (BoolQueryBuilder) boolQueryBuilder.should().get(1);
        final BoolQueryBuilder shouldQueryBuilder3 = (BoolQueryBuilder) boolQueryBuilder.should().get(2);
        final BoolQueryBuilder shouldQueryBuilder4 = (BoolQueryBuilder) boolQueryBuilder.should().get(3);
        final BoolQueryBuilder shouldQueryBuilder5 = (BoolQueryBuilder) boolQueryBuilder.should().get(4);

        assertThat(shouldQueryBuilder1.must(), hasSize(3));
        assertThat(shouldQueryBuilder1.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(1), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder1.must().get(2), instanceOf((MatchQueryBuilder.class)));

        MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder1.must().get(2);
        assertThat(matchQueryBuilder.fieldName(), is("parties.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(nullValue()));

        assertThat(shouldQueryBuilder2.must(), hasSize(3));
        assertThat(shouldQueryBuilder2.must().get(0), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(1), instanceOf((MatchQueryBuilder.class)));
        assertThat(shouldQueryBuilder2.must().get(2), instanceOf((MatchQueryBuilder.class)));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);


        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName"));
        assertThat(matchQueryBuilder.value(), is("John"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder2.must().get(2);
        assertThat(matchQueryBuilder.fieldName(), is("parties.middleName"));
        assertThat(matchQueryBuilder.value(), is("Smith"));
        assertThat(matchQueryBuilder.operator(), is(OR));
        assertThat(matchQueryBuilder.boost(), is(1.0F));
        assertThat(matchQueryBuilder.fuzziness(), is(Fuzziness.AUTO));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder3.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        MultiMatchQueryBuilder multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder3.must().get(1);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(AND));
        assertThat(multiMatchQueryBuilder.boost(), is(0.4F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder4.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        multiMatchQueryBuilder = (MultiMatchQueryBuilder) shouldQueryBuilder4.must().get(1);
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.firstName", 2.0F)));
        assertThat(multiMatchQueryBuilder.fields(), is(hasEntry("parties.middleName", 2.0F)));
        assertThat(multiMatchQueryBuilder.value(), is("John Smith"));
        assertThat(multiMatchQueryBuilder.type(), is(CROSS_FIELDS));
        assertThat(multiMatchQueryBuilder.operator(), is(OR));
        assertThat(multiMatchQueryBuilder.boost(), is(0.3F));
        assertThat(multiMatchQueryBuilder.fuzziness(), is(nullValue()));

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(0);
        assertPostCodeQueryBuilder(matchQueryBuilder);

        matchQueryBuilder = (MatchQueryBuilder) shouldQueryBuilder5.must().get(1);
        assertThat(matchQueryBuilder.fieldName(), is("parties.firstName.ngrammed"));
        assertThat(matchQueryBuilder.value(), is("John"));
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