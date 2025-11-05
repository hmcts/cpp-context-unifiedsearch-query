package uk.gov.moj.cpp.unifiedsearch.query.api.accesscontrol;

import static uk.gov.moj.cpp.accesscontrol.drools.ExpectedPermission.builder;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.moj.cpp.accesscontrol.drools.ExpectedPermission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PermissionConstants {
    private static final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private PermissionConstants() {
    }

    public static String[] getPermissionsForViewCPSSearch() throws JsonProcessingException {
        final ExpectedPermission expectedPermission = builder()
                .withObject("CPS Search")
                .withAction("View")
                .build();
        return new String[]{objectMapper.writeValueAsString(expectedPermission)};
    }

    public static String[] getPermissionsForViewCPSSearchRestricted() throws JsonProcessingException {
        final ExpectedPermission expectedPermission = builder()
                .withObject("CPS Search Restricted")
                .withAction("View")
                .build();
        return new String[]{objectMapper.writeValueAsString(expectedPermission)};
    }
}
