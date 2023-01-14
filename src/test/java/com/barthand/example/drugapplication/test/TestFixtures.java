package com.barthand.example.drugapplication.test;

import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
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
    
    public static CreateDrugApplicationRequest fullyPopulatedCreateDrugApplicationRequest() {
        return new CreateDrugApplicationRequest("create-app-number", "create-manufacturer-name",
                "create-substance-name", List.of("create-product-001", "create-product-002") 
        );
    }

    public static DrugApplicationAggregate fullyPopulatedDrugApplicationAggregate() {
        return DrugApplicationAggregate.builder()
                .applicationNumber("create-app-number")
                .manufacturerName("create-manufacturer-name")
                .substanceName("create-substance-name")
                .productNames(List.of("create-product-001", "create-product-002"))
                .build(); 
    }
}
