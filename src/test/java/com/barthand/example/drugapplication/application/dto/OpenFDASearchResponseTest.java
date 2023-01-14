package com.barthand.example.drugapplication.application.dto;

import com.barthand.example.drugapplication.test.TestFixtures;
import com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

class OpenFDASearchResponseTest {

    @ParameterizedTest
    @MethodSource("nullableFDADrugResult")
    void shouldMapFromNullableOpenFDADrugResult(OpenFDADrugsClientResponse.OpenFDADrugResult drugResult) {
        // when
        OpenFDASearchResponse result = OpenFDASearchResponse.from(drugResult);

        // then
        Assertions.assertEquals(
                new OpenFDASearchResponse(null, null, emptyList(), emptyList())
                , result);
    }

    @Test
    void shouldMapFromFullyPopulatedOpenFDADrugResult() {
        // given
        OpenFDADrugsClientResponse.OpenFDADrugResult drugResult = TestFixtures.fullyPopulatedOpenFDADrugResult();

        // when
        OpenFDASearchResponse result = OpenFDASearchResponse.from(drugResult);

        // then
        Assertions.assertEquals(new OpenFDASearchResponse("some-app-number", "some-sponsor-name",
                        List.of(
                                new OpenFDASearchResponse.Product("product-number-001", "some-brand-name", "some-marketing-status", "some-dosage-form"),
                                new OpenFDASearchResponse.Product("product-number-002", "another-brand-name", "another-marketing-status", "another-dosage-form")
                        ), List.of("manufacturer-001", "manufacturer-002"))
                , result);
    }

    private static Stream<Arguments> nullableFDADrugResult() {
        return Stream.of(
                Arguments.of(TestFixtures.nullsEverywhereOpenFDADrugResult()),
                Arguments.of(TestFixtures.nullsInMiddleOpenFDADrugResult())
        );
    }
}