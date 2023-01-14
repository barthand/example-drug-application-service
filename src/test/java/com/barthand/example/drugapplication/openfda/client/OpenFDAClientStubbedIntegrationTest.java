package com.barthand.example.drugapplication.openfda.client;

import com.barthand.example.drugapplication.infrastructure.BaseIntegrationTest;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureWireMock(port = 0)
class OpenFDAClientStubbedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OpenFDAClient openFDAClient;

    @Test
    void shouldIssueRequestAtExpectedUriAndCorrectlyParseOpenFDADrugsResponse() {
        // given
        SearchParams searchParams = SearchParams.start()
                .termMatchIfNotBlank(OpenFDAClient.KnownSearchableFields.MANUFACTURER_NAME, "someCompany")
                .termMatchIfNotBlank(OpenFDAClient.KnownSearchableFields.BRAND_NAME, "someBrand")
                .paging()
                .limit(5)
                .skip(10)
                .and().sortDescending("application_number")
                .build();

        // when
        OpenFDADrugsClientResponse searchResult = openFDAClient.searchDrugsEndpoint(searchParams);

        // then
        assertEquals(new OpenFDADrugsClientResponse(new OpenFDADrugsClientResponse.Meta(
                new OpenFDADrugsClientResponse.PageMetadata(10, 5, 12987)
        ), List.of(
                new OpenFDADrugsClientResponse.OpenFDADrugResult(
                        "NDA017395", "HOSPIRA", List.of(
                        new OpenFDADrugsClientResponse.Product("003", "INTROPIN", "Discontinued", "INJECTABLE"),
                        new OpenFDADrugsClientResponse.Product("001", "INTROPIN", "Discontinued", "INJECTABLE"),
                        new OpenFDADrugsClientResponse.Product("002", "INTROPIN", "Discontinued", "INJECTABLE")
                ), null
                ),
                new OpenFDADrugsClientResponse.OpenFDADrugResult("NDA017439", "ALLERGAN", List.of(
                        new OpenFDADrugsClientResponse.Product("001", "HYDROXYPROGESTERONE CAPROATE", "Discontinued", "INJECTABLE"),
                        new OpenFDADrugsClientResponse.Product("002", "HYDROXYPROGESTERONE CAPROATE", "Discontinued", "INJECTABLE")
                ), null
                ),
                new OpenFDADrugsClientResponse.OpenFDADrugResult("ANDA076822", "TEVA PHARMS USA", List.of(
                        new OpenFDADrugsClientResponse.Product("001", "RABEPRAZOLE SODIUM", "Discontinued", "TABLET, DELAYED RELEASE")
                ), null
                ),
                new OpenFDADrugsClientResponse.OpenFDADrugResult("ANDA077112", "PLIVA HRVATSKA DOO", List.of(
                        new OpenFDADrugsClientResponse.Product("002", "ONDANSETRON HYDROCHLORIDE", "Discontinued", "TABLET"),
                        new OpenFDADrugsClientResponse.Product("003", "ONDANSETRON HYDROCHLORIDE", "Discontinued", "TABLET"),
                        new OpenFDADrugsClientResponse.Product("001", "ONDANSETRON HYDROCHLORIDE", "Discontinued", "TABLET")
                ), null
                ),
                new OpenFDADrugsClientResponse.OpenFDADrugResult("ANDA078312", "AMNEAL PHARMS", List.of(
                        new OpenFDADrugsClientResponse.Product("001", "RANITIDINE HYDROCHLORIDE", "Discontinued", "SYRUP")
                ), new OpenFDADrugsClientResponse.OpenFDAAnnotations(List.of("Amneal Pharmaceuticals LLC"))
                )
        )
        ), searchResult);
    }

    @BeforeEach
    public void stubbing() {
        WireMock.stubFor(WireMock.get("/drug/drugsfda.json?search=openfda.manufacturer_name%3A%22someCompany%22%20AND%20openfda.brand_name%3A%22someBrand%22&limit=5&skip=10&sort=application_number%3Adesc")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("openfda-drugs-example-response.json")
                )
        );
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("openfda.url", () -> "http://localhost:${wiremock.server.port}");
    }

}