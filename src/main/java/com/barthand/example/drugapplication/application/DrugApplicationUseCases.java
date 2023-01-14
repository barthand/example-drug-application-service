package com.barthand.example.drugapplication.application;

import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import com.barthand.example.drugapplication.application.dto.OpenFDASearchResponse;
import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DrugApplicationUseCases {
    
    // `org.springframework.data.domain.Page` and `org.springframework.data.domain.Pageable` are introduced here 
    // as a dependencies to the domain code, which might (and probably should) be avoided otherwise. 
    Page<OpenFDASearchResponse> searchForApplicationsSubmittedToFDA(Pageable pageable, String manufacturerName, String fdaBrandName);
    
    Page<DrugApplicationAggregate> listDrugApplications(Pageable pageable);

    Optional<DrugApplicationAggregate> findDrugApplication(String applicationNumber);

    DrugApplicationAggregate saveDrugApplication(CreateDrugApplicationRequest drugApplication);
    
}
