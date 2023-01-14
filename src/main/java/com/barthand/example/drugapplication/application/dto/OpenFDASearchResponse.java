package com.barthand.example.drugapplication.application.dto;

import com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record OpenFDASearchResponse(String applicationNumber, String sponsorName, 
                                    List<Product> products, List<String> manufacturerNames) {
    
    public static OpenFDASearchResponse from(OpenFDADrugsClientResponse.OpenFDADrugResult drugResult) {
        return new OpenFDASearchResponse(drugResult.applicationNumber(), drugResult.sponsorName(),
                Optional.ofNullable(drugResult.products()).orElse(Collections.emptyList()).stream().map(Product::from).toList(),
                Optional.ofNullable(drugResult.openfda()).map(OpenFDADrugsClientResponse.OpenFDAAnnotations::manufacturerName).orElse(Collections.emptyList()));
    }

    public record Product(String productNumber, String brandName, String marketingStatus, String dosageForm) {
        public static Product from(OpenFDADrugsClientResponse.Product product) {
            return new Product(product.productNumber(), product.brandName(), product.marketingStatus(), product.dosageForm());
        }    
    }
}
