package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.AddressSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Disabled
public class PerformanceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceIT.class);

    private SearchApiClient searchApiClient = SearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static AddressSearchDataIngester addressSearchDataIngester = new AddressSearchDataIngester();
    private final StopWatch stopWatch = new StopWatch();

    private static final String PARTY_ADDRESS = "partyAddress";

    private final static int REQUEST_COUNT = 100;

    final AtomicInteger integer = new AtomicInteger();

    @BeforeEach
    public void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        addressSearchDataIngester.loadCasesAddressData();
    }

    @Test
    public void shouldSendMultipleQueries() {
        integer.getAndIncrement();
        try {
            final Map<String, String> parameters = of(PARTY_ADDRESS, "Liverpool");
            final List<CaseSearchResponse> responseList = new ArrayList<>();

            stopWatch.start();

            for (int i = 0; i < REQUEST_COUNT; i++) {
                final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
                responseList.add(caseSearchResponse);
            }

            stopWatch.stop();

            assertThat(responseList.size(), is(REQUEST_COUNT));
            responseList.forEach(caseSearchResponse -> assertThat(caseSearchResponse.getCases(), hasSize(6)));

            LOGGER.info("Queries sent to elasticsearch: " + REQUEST_COUNT);
            LOGGER.info("Average execution time per query: " + stopWatch.getTime() / REQUEST_COUNT + " milliseconds");
            LOGGER.info("Total execution time " + stopWatch.getTime() + " milliseconds" + " for " + REQUEST_COUNT);
        } catch (final IOException e) {
            e.getStackTrace();
        }
    }
}
