package com.barthand.example.drugapplication.domain;

import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(DrugApplicationAggregate.COLLECTION_NAME)
@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class DrugApplicationAggregate {
    
    public static final String COLLECTION_NAME = "drug_applications";
    
    public static final String FIELD_MANUFACTURER_NAME = "manufacturerName";
    
    @Id
    private final String applicationNumber;

    private final String manufacturerName;
    
    private final String substanceName;
    
    private final List<String> productNames;

    public static DrugApplicationAggregate create(CreateDrugApplicationRequest createDrugApplicationRequest) {
        return new DrugApplicationAggregate(
                createDrugApplicationRequest.getApplicationNumber(), 
                createDrugApplicationRequest.getManufacturerName(),
                createDrugApplicationRequest.getSubstanceName(), 
                createDrugApplicationRequest.getProductNames()
        );
    }
}
