package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AliasDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.io.IOException;
import java.util.List;

public class NameQueryDataIngester {

    public static final String NAME_ALIAS = "Abrahamson";
    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList) throws IOException {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }

    public static List<CaseDocument> caseListForLastNameQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(10);

        final PartyDocument.Builder markSpencerBuilder = defaultPartyAsBuilder().withFirstName("Mark").withLastName("Spencer").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder spencerMarkBuilder = defaultPartyAsBuilder().withFirstName("Spencer").withMiddleName(null).withOrganisationName(null).withLastName("Mark");

        final PartyDocument.Builder marksSpencersBuilder = defaultPartyAsBuilder().withFirstName("Marks").withMiddleName(null).withOrganisationName(null).withLastName("Spencers");
        final PartyDocument.Builder spencersMarksBuilder = defaultPartyAsBuilder().withFirstName("Spencers").withLastName("Marks").withMiddleName(null).withOrganisationName(null);

        final PartyDocument.Builder markSpencersBuilder = defaultPartyAsBuilder().withFirstName("Mark").withLastName("Spencers").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder spencersMarkBuilder = defaultPartyAsBuilder().withFirstName("Spencers").withMiddleName(null).withOrganisationName(null).withLastName("Mark");

        final PartyDocument.Builder marksSpencerBuilder = defaultPartyAsBuilder().withFirstName("Marks").withLastName("Spencer").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder spencerMarksBuilder = defaultPartyAsBuilder().withFirstName("Spencer").withMiddleName(null).withOrganisationName(null).withLastName("Marks");

        final PartyDocument.Builder markAndSpencerBuilder = defaultPartyAsBuilder().withFirstName("x").withLastName("Mark And Spencer").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder marksAndSpencersBuilder = defaultPartyAsBuilder().withFirstName("x").withLastName("Marks And Spencers").withMiddleName(null).withOrganisationName(null);


        // Note the party names are added here in the order of expected relevance/order from the query:
        caseBuilderList.get(0).withParties(asList(markSpencerBuilder));
        caseBuilderList.get(1).withParties(asList(spencerMarkBuilder));
        caseBuilderList.get(2).withParties(asList(marksSpencersBuilder));
        caseBuilderList.get(3).withParties(asList(spencersMarksBuilder));
        caseBuilderList.get(4).withParties(asList(markSpencersBuilder));
        caseBuilderList.get(5).withParties(asList(spencersMarkBuilder));
        caseBuilderList.get(6).withParties(asList(marksSpencerBuilder));
        caseBuilderList.get(7).withParties(asList(spencerMarksBuilder));
        caseBuilderList.get(8).withParties(asList(markAndSpencerBuilder));
        caseBuilderList.get(9).withParties(asList(marksAndSpencersBuilder));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());


    }

    public static List<CaseDocument> caseListForNameQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(12);

        final PartyDocument.Builder johnSmithBuilder = defaultPartyAsBuilder().withFirstName("John").withLastName("Smith").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder johnAllenSmithBuilder = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder johnLuisSmithBuilder = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Luis").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder markSmithBuilder = defaultPartyAsBuilder().withFirstName("Mark").withLastName("Smith").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder markAllenSmithBuilder = defaultPartyAsBuilder().withFirstName("Mark").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder markLuisSmithBuilder = defaultPartyAsBuilder().withFirstName("Mark").withMiddleName("Luis").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder jamesSmithsonBuilder = defaultPartyAsBuilder().withFirstName("James").withLastName("Smithson").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder johnCraikBuilder = defaultPartyAsBuilder().withFirstName("John").withLastName("Craik").withMiddleName(null).withOrganisationName(null);
        final PartyDocument.Builder johnAndrewSmithBuilder = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Andrew").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder johnSmithAllenBuilder = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Smith").withOrganisationName(null).withLastName("Allen");
        final PartyDocument.Builder johnAllanSmithBuilder = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Allan").withOrganisationName(null).withLastName("Smith");
        final PartyDocument.Builder johnsAllenSmithBuilder = defaultPartyAsBuilder().withFirstName("Johns").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith");

        // Note the party names are added here in the order of expected relevance/order from the query:
        caseBuilderList.get(0).withParties(asList(johnAllenSmithBuilder));
        caseBuilderList.get(1).withParties(asList(johnsAllenSmithBuilder));
        caseBuilderList.get(2).withParties(asList(johnAllanSmithBuilder));
        caseBuilderList.get(3).withParties(asList(johnSmithAllenBuilder));
        caseBuilderList.get(4).withParties(asList(johnLuisSmithBuilder));
        caseBuilderList.get(5).withParties(asList(johnSmithBuilder));
        caseBuilderList.get(6).withParties(asList(johnAndrewSmithBuilder));
        caseBuilderList.get(7).withParties(asList(johnCraikBuilder));
        caseBuilderList.get(8).withParties(asList(markAllenSmithBuilder));
        caseBuilderList.get(9).withParties(asList(markLuisSmithBuilder));
        caseBuilderList.get(10).withParties(asList(markSmithBuilder));
        caseBuilderList.get(11).withParties(asList(jamesSmithsonBuilder));


        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());


    }

    public static List<CaseDocument> caseListForOrganisationNameQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(5);

        final PartyDocument.Builder spencerBoydBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("spencer Boyd");
        final PartyDocument.Builder marksAndSpencerBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("marks and spencer");
        final PartyDocument.Builder markAndSpencerBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("mark and spencer");
        final PartyDocument.Builder markBoydBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("mark Boyd");
        final PartyDocument.Builder markBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("mark");

        caseBuilderList.get(0).withParties(asList(spencerBoydBuilder));
        caseBuilderList.get(1).withParties(asList(marksAndSpencerBuilder));
        caseBuilderList.get(2).withParties(asList(markAndSpencerBuilder));
        caseBuilderList.get(3).withParties(asList(markBoydBuilder));
        caseBuilderList.get(4).withParties(asList(markBuilder));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());


    }

    public static List<CaseDocument> caseListForPartyNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createParty("Abbot", NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    private static PartyDocument.Builder createParty(final String name, final String alias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(PartyType.DEFENDANT.name())
                .withOrganisationName(name)
                .withAliases(asList(new AliasDocument(alias)));
        return partyBuilder;
    }

    public static List<CaseDocument> caseListForFirstNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForFirstName("Abbot", NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public static List<CaseDocument> caseListForMiddleNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForMiddleName("Abbot", NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public static List<CaseDocument> caseListForFirstAndMiddleNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForFirstAndMiddleName("AbbotFirstName", "AbbotMiddleName", NAME_ALIAS, NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public static List<CaseDocument> caseListForLastNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForLastName("Abbot", NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public static List<CaseDocument> caseListForOrganisationNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForOrganisationName("Abbot", NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public static List<CaseDocument> caseListForLastOrOrganisationNameQueryWithAlias() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);

        caseBuilderList.get(0).withParties(asList(createPartyAliasForLastOrOrganisationName("AbbotLastName", "AbbotOrganisationName", NAME_ALIAS, NAME_ALIAS)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    private static PartyDocument.Builder createPartyAliasForLastOrOrganisationName(final String lastName,
                                                                                   final String organisationName,
                                                                                   final String lastNameAlias,
                                                                                   final String organisationNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(
                PartyType.DEFENDANT.name())
                .withLastName(lastName)
                .withOrganisationName(organisationName)
                .withAliases(asList(new AliasDocument("", "", lastNameAlias), new AliasDocument(organisationNameAlias)));
        return partyBuilder;
    }

    private static PartyDocument.Builder createPartyAliasForFirstName(final String firstName, final String firstNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(PartyType.DEFENDANT.name())
                .withFirstName(firstName)
                .withAliases(asList(new AliasDocument(firstNameAlias, "", "")));
        return partyBuilder;
    }

    private static PartyDocument.Builder createPartyAliasForMiddleName(final String middleName, final String middleNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(PartyType.DEFENDANT.name())
                .withMiddleName(middleName)
                .withAliases(asList(new AliasDocument("", middleNameAlias, "")));
        return partyBuilder;
    }


    private static PartyDocument.Builder createPartyAliasForLastName(final String lastName, final String lastNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(PartyType.DEFENDANT.name())
                .withMiddleName(lastName)
                .withAliases(asList(new AliasDocument("", "", lastNameAlias)));
        return partyBuilder;
    }

    private static PartyDocument.Builder createPartyAliasForOrganisationName(final String organisationName, final String organisationNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(PartyType.DEFENDANT.name())
                .withOrganisationName(organisationName)
                .withAliases(asList(new AliasDocument(organisationNameAlias)));
        return partyBuilder;
    }

    private static PartyDocument.Builder createPartyAliasForFirstAndMiddleName(final String firstName,
                                                                               final String middleName,
                                                                               final String firstNameAlias,
                                                                               final String middleNameAlias) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(
                PartyType.DEFENDANT.name())
                .withFirstName(firstName)
                .withMiddleName(middleName)
                .withAliases(asList(new AliasDocument(firstNameAlias, middleNameAlias, "")));
        return partyBuilder;
    }

}
