package com.barthand.example.drugapplication.openfda.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record OpenFDADrugsClientResponse(Meta meta, List<OpenFDADrugResult> results) {

    // @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) could be used instead @JsonProperty(), but this doesn't work yet with records:
    // https://github.com/FasterXML/jackson-databind/issues/2992
    public record OpenFDADrugResult(@JsonProperty("application_number") String applicationNumber,
                                    @JsonProperty("sponsor_name") String sponsorName, List<Product> products,
                                    OpenFDAAnnotations openfda) { }

    public record Product(@JsonProperty("product_number") String productNumber, @JsonProperty("brand_name") String brandName, 
                          @JsonProperty("marketing_status") String marketingStatus, @JsonProperty("dosage_form") String dosageForm) { }

    public record OpenFDAAnnotations(@JsonProperty("manufacturer_name") List<String> manufacturerName) { }


    public record Meta(PageMetadata results) { }

    public record PageMetadata(int skip, int limit, int total) { }

}
