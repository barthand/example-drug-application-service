package com.barthand.example.drugapplication.api;

import com.barthand.example.drugapplication.application.DrugApplicationUseCases;
import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "drug applications", description = "provides operations over drug applications stored within this system")
@RequestMapping("/v1")
public class DrugApplicationsController {
    
    private final DrugApplicationUseCases drugApplicationUseCases;

    @PostMapping(value = "/applications", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "create Drug Application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid params supplied", content = @Content),
    })
    public DrugApplicationAggregate createDrugApplication(@RequestBody @Valid CreateDrugApplicationRequest createDrugApplicationRequest) {
        return drugApplicationUseCases.saveDrugApplication(createDrugApplicationRequest);
    }

    @GetMapping(value = "/applications", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "searches for Drug Applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid params supplied", content = @Content)
    })
    public Page<DrugApplicationAggregate> listDrugApplications(
            @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        return drugApplicationUseCases.listDrugApplications(pageable);
    }

    @GetMapping(value = "/applications/{applicationNumber}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "finds Drug Applications by application number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid params supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No applications are matched", content = @Content)
    })
    public ResponseEntity<DrugApplicationAggregate> findDrugApplications(@PathVariable String applicationNumber) {
        Optional<DrugApplicationAggregate> drugApplication = drugApplicationUseCases.findDrugApplication(applicationNumber);
        return drugApplication.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
}
