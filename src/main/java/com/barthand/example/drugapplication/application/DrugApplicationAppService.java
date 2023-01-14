package com.barthand.example.drugapplication.application;

import com.barthand.example.drugapplication.application.dto.OpenFDASearchResponse;
import com.barthand.example.drugapplication.openfda.client.OpenFDAClient;
import com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse;
import com.barthand.example.drugapplication.openfda.client.SearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DrugApplicationAppService implements DrugApplicationUseCases {

    private final OpenFDAClient openFDAClient;

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
}
