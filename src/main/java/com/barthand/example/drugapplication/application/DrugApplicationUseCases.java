package com.barthand.example.drugapplication.application;

import com.barthand.example.drugapplication.application.dto.OpenFDASearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrugApplicationUseCases {
    
    // I've introduced here `org.springframework.data.domain.Page` and `org.springframework.data.domain.Pageable` 
    // as a dependency to the domain code, which might (and probably should) be avoided otherwise. 
    Page<OpenFDASearchResponse> searchForApplicationsSubmittedToFDA(Pageable pageable, String manufacturerName, String fdaBrandName);
    
    
    
}
