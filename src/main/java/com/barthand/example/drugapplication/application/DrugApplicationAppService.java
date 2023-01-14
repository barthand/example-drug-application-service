package com.barthand.example.drugapplication.application;

import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import com.barthand.example.drugapplication.application.dto.OpenFDASearchResponse;
import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import com.barthand.example.drugapplication.domain.port.DrugApplicationRepository;
import com.barthand.example.drugapplication.openfda.client.OpenFDAClient;
import com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse;
import com.barthand.example.drugapplication.openfda.client.SearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DrugApplicationAppService implements DrugApplicationUseCases {

    private final OpenFDAClient openFDAClient;

    private final DrugApplicationRepository drugApplicationRepository;

    @Override
    public Page<OpenFDASearchResponse> searchForApplicationsSubmittedToFDA(Pageable pageable, String manufacturerName, String fdaBrandName) {
        SearchParams searchParams = SearchParams.start()
                .termMatchIfNotBlank(OpenFDAClient.KnownSearchableFields.MANUFACTURER_NAME, manufacturerName)
                .termMatchIfNotBlank(OpenFDAClient.KnownSearchableFields.BRAND_NAME, fdaBrandName)
                .paging()
                .limit(pageable.getPageSize()).skip(pageable.getOffset())
                .and()
                .sortUsing(pageable.getSort().stream().findFirst())
                .build();
        OpenFDADrugsClientResponse openFDADrugsResponse = openFDAClient.searchDrugsEndpoint(searchParams);

        List<OpenFDASearchResponse> result = openFDADrugsResponse.results().stream()
                .map(OpenFDASearchResponse::from)
                .toList();

        return new PageImpl<>(result, pageable, openFDADrugsResponse.meta().results().total());
    }

    @Override
    public Page<DrugApplicationAggregate> listDrugApplications(Pageable pageable) {
        return drugApplicationRepository.findAll(pageable);
    }

    @Override
    public Optional<DrugApplicationAggregate> findDrugApplication(String applicationNumber) {
        return drugApplicationRepository.findById(applicationNumber);
    }

    @Override
    public DrugApplicationAggregate saveDrugApplication(CreateDrugApplicationRequest createDrugApplicationRequest) {
        return drugApplicationRepository.save(DrugApplicationAggregate.create(createDrugApplicationRequest));
    }

}
