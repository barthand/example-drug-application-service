package com.barthand.example.drugapplication.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateDrugApplicationRequest {

    @NotBlank
    private final String applicationNumber;

    private final String manufacturerName;

    private final String substanceName;

    private final List<String> productNames;

}
