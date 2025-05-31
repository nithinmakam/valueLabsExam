package com.valuelabs.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@Schema(description = "Request object for generating a tracking number")
public class TrackingNumberRequest {
    @Schema(description = "ISO 3166-1 alpha-2 country code for origin", example = "MY")
    @Pattern(regexp = "[A-Z]{2}", message = "Invalid origin country code")
    @NotBlank(message = "Origin country ID is required")
    private String originCountryId;
    
    @Schema(description = "ISO 3166-1 alpha-2 country code for destination", example = "ID")
    @Pattern(regexp = "[A-Z]{2}", message = "Invalid destination country code")
    @NotBlank(message = "Destination country ID is required")
    private String destinationCountryId;
    
    @Schema(description = "Weight in kilograms (up to 3 decimal places)", example = "1.234")
    @Positive(message = "Weight must be positive")
    @Digits(integer = 4, fraction = 3, message = "Invalid weight format")
    @NotNull(message = "Weight is required")
    private Double weight;
    
    @Schema(description = "Order creation timestamp in RFC 3339 format", example = "2018-11-20T19:29:32+08:00")
    @NotNull(message = "Created at timestamp is required")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneId.systemDefault()); 
    
    @Schema(description = "Customer UUID", example = "de619854-b59b-425e-9db4-943979e1bd49")
    @NotNull(message = "Customer ID is required")
    private UUID customerId;
    
    @Schema(description = "Customer name", example = "RedBox Logistics")
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @Schema(description = "Customer slug in kebab-case", example = "redbox-logistics")
    @Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "Invalid customer slug")
    @NotBlank(message = "Customer slug is required")
    private String customerSlug;

}