package com.valuelabs.exam.controller;

import com.valuelabs.exam.dto.TrackingNumberRequest;
import com.valuelabs.exam.dto.TrackingNumberResponse;
import com.valuelabs.exam.service.TrackingNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@Tag(name = "Tracking Number API", description = "API for generating unique tracking numbers")
@Validated
public class TrackingNumberController {
    
    @Autowired
    private TrackingNumberService service;
    
    @Operation(summary = "Generate a new tracking number", 
              description = "Generates a unique tracking number based on provided query parameters")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully generated tracking number"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/next-tracking-number")
    public ResponseEntity<TrackingNumberResponse> getNextTrackingNumber(
            @Parameter(description = "ISO 3166-1 alpha-2 country code for origin", example = "MY")
            @RequestParam @Pattern(regexp = "[A-Z]{2}", message = "Invalid origin country code") 
            @NotBlank(message = "Origin country ID is required") String originCountryId,
            
            @Parameter(description = "ISO 3166-1 alpha-2 country code for destination", example = "ID")
            @RequestParam @Pattern(regexp = "[A-Z]{2}", message = "Invalid destination country code") 
            @NotBlank(message = "Destination country ID is required") String destinationCountryId,
            
            @Parameter(description = "Weight in kilograms (up to 3 decimal places)", example = "1.234")
            @RequestParam @Positive(message = "Weight must be positive") 
            @Digits(integer = 4, fraction = 3, message = "Invalid weight format") 
            @NotNull(message = "Weight is required") Double weight,
           
            @Parameter(description = "Customer UUID", example = "de619854-b59b-425e-9db4-943979e1bd49")
            @RequestParam @NotNull(message = "Customer ID is required") UUID customerId,
            
            @Parameter(description = "Customer name", example = "RedBox Logistics")
            @RequestParam @NotBlank(message = "Customer name is required") String customerName,
            
            @Parameter(description = "Customer slug in kebab-case", example = "redbox-logistics")
            @RequestParam @Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "Invalid customer slug") 
            @NotBlank(message = "Customer slug is required") String customerSlug) {
        try {
            TrackingNumberRequest request = new TrackingNumberRequest();
            request.setOriginCountryId(originCountryId);
            request.setDestinationCountryId(destinationCountryId);
            request.setWeight(weight);
            //request.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
            request.setCustomerId(customerId);
            request.setCustomerName(customerName);
            request.setCustomerSlug(customerSlug);
            
            TrackingNumberResponse response = service.generateTrackingNumber(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            TrackingNumberResponse errorResponse = new TrackingNumberResponse();
            errorResponse.setTrackingNumber("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}