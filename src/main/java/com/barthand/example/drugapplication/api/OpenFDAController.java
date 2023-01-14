package com.barthand.example.drugapplication.api;

import com.barthand.example.drugapplication.application.DrugApplicationUseCases;
import com.barthand.example.drugapplication.application.dto.OpenFDASearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "openfda", description = "wrapper for openfda API")
public class OpenFDAController {

    private final DrugApplicationUseCases drugApplicationUseCases;
    
    @GetMapping(value = "/openfda/applications", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "searches for Drug Applications in OpenFDA API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid params supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No applications are matched", content = @Content)
    })
    public Page<OpenFDASearchResponse> getOpenFDADrugApplications(@ParameterObject @PageableDefault(size = 5) Pageable pageable,
                                                                                         @RequestParam String manufacturerName,
                                                                                         @RequestParam(required = false) String fdaBrandName) {
        return drugApplicationUseCases.searchForApplicationsSubmittedToFDA(pageable, manufacturerName, fdaBrandName);
    }
    
}
