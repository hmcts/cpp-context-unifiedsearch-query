package uk.gov.moj.cpp.unifiedsearch.query.builders;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CRIME_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;

import uk.gov.justice.services.cdi.QualifierAnnotationExtractor;
import uk.gov.justice.services.unifiedsearch.UnifiedSearchName;
import uk.gov.moj.cpp.unifiedsearch.query.builders.service.CaseQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.service.CpsCaseQueryBuilderService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@ApplicationScoped
class UnifiedSearchQueryBuilderProducer {

    @Inject
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;

    @Inject
    private CaseQueryBuilderService caseQueryBuilderService;

    @Inject
    private CpsCaseQueryBuilderService cpsCaseQueryBuilderService;

    @Produces
    @UnifiedSearchName
    UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService(final InjectionPoint injectionPoint) {
        final String indexName = qualifierAnnotationExtractor.getFrom(injectionPoint, UnifiedSearchName.class).value();
        if (CRIME_CASE_INDEX_NAME.equals(indexName)) {
            return caseQueryBuilderService;
        }
        if (CPS_CASE_INDEX_NAME.equals(indexName)) {
            return cpsCaseQueryBuilderService;
        }
        throw new InjectionException(format("No UnifiedSearchQueryBuilderService specified for index name: %s", indexName));
    }

}
