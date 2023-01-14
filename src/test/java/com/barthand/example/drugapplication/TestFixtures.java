package com.barthand.example.drugapplication;

import com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse;

import java.util.List;

public class TestFixtures {
    public static OpenFDADrugsClientResponse.OpenFDADrugResult nullsEverywhereOpenFDADrugResult() {
        return new OpenFDADrugsClientResponse.OpenFDADrugResult(null, null, null, null);
    }

    public static OpenFDADrugsClientResponse.OpenFDADrugResult nullsInMiddleOpenFDADrugResult() {
        return new OpenFDADrugsClientResponse.OpenFDADrugResult(null, null, null, 
                new OpenFDADrugsClientResponse.OpenFDAAnnotations(null));
    }

    public static OpenFDADrugsClientResponse.OpenFDADrugResult fullyPopulatedOpenFDADrugResult() {
        return new OpenFDADrugsClientResponse.OpenFDADrugResult(
                "some-app-number", "some-sponsor-name", 
                List.of(
                        new OpenFDADrugsClientResponse.Product("product-number-001", "some-brand-name", "some-marketing-status", "some-dosage-form"),
                        new OpenFDADrugsClientResponse.Product("product-number-002", "another-brand-name", "another-marketing-status", "another-dosage-form")
                ), 
                new OpenFDADrugsClientResponse.OpenFDAAnnotations(List.of("manufacturer-001", "manufacturer-002")));
    }
}
