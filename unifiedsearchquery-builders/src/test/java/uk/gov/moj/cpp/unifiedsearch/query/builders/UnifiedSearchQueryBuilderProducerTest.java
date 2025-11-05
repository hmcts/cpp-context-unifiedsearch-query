package uk.gov.moj.cpp.unifiedsearch.query.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CRIME_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;

import uk.gov.justice.services.cdi.QualifierAnnotationExtractor;
import uk.gov.justice.services.unifiedsearch.UnifiedSearchName;
import uk.gov.moj.cpp.unifiedsearch.query.builders.service.CaseQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.service.CpsCaseQueryBuilderService;

import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnifiedSearchQueryBuilderProducerTest {

    @Mock
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;

    @Mock
    private CaseQueryBuilderService caseQueryBuilderService;

    @Mock
    private CpsCaseQueryBuilderService cpsCaseQueryBuilderService;

    @InjectMocks
    private UnifiedSearchQueryBuilderProducer unifiedSearchQueryBuilderProducer;

    private InjectionPoint injectionPoint;
    private UnifiedSearchName unifiedSearchName;

    @BeforeEach
    public void setUp() {
        injectionPoint = mock(InjectionPoint.class);
        unifiedSearchName = mock(UnifiedSearchName.class);
    }

    @Test
    public void shouldProduceUnifiedSearchQueryBuilder() {

        when(qualifierAnnotationExtractor.getFrom(injectionPoint, UnifiedSearchName.class)).thenReturn(unifiedSearchName);
        when(unifiedSearchName.value()).thenReturn(CRIME_CASE_INDEX_NAME);

        assertThat(unifiedSearchQueryBuilderProducer.unifiedSearchQueryBuilderService(injectionPoint), is(caseQueryBuilderService));
    }

    @Test
    public void shouldThrowExceptionIfIndexNameNotFound() {


        when(qualifierAnnotationExtractor.getFrom(injectionPoint, UnifiedSearchName.class)).thenReturn(unifiedSearchName);
        when(unifiedSearchName.value()).thenReturn("unknown_index");

        final InjectionException injectionException = assertThrows(
                InjectionException.class,
                () -> unifiedSearchQueryBuilderProducer.unifiedSearchQueryBuilderService(injectionPoint));

        assertThat(injectionException.getMessage(), is("No UnifiedSearchQueryBuilderService specified for index name: unknown_index"));
    }

    @Test
    public void shouldProduceCpsSearchQueryBuilder() {

        when(qualifierAnnotationExtractor.getFrom(injectionPoint, UnifiedSearchName.class)).thenReturn(unifiedSearchName);
        when(unifiedSearchName.value()).thenReturn(CPS_CASE_INDEX_NAME);

        assertThat(unifiedSearchQueryBuilderProducer.unifiedSearchQueryBuilderService(injectionPoint), is(cpsCaseQueryBuilderService));
    }
}
