package com.valuelabs.exam.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Schema(description = "Response object containing the generated tracking number")
public class TrackingNumberResponse {
    @Schema(description = "Generated tracking number", example = "de619854-b59b-425e-9db4-943979e1bd49")
    private String trackingNumber;
    
    @Schema(description = "Origin country code", example = "MY")
    private String originCountryId;
    
    @Schema(description = "Destination country code", example = "ID")
    private String destinationCountryId;
    
    @Schema(description = "Weight in kilograms", example = "1.234")
    private Double weight;
    
    @Schema(description = "Customer UUID", example = "de619854-b59b-425e-9db4-943979e1bd49")
    private String customerId;
    
    @Schema(description = "Customer name", example = "RedBox Logistics")
    private String customerName;
    
    @Schema(description = "Customer slug", example = "redbox-logistics")
    private String customerSlug;
    
    @Schema(description = "Order creation timestamp", example = "2018-11-20T19:29:32+08:00")
    private OffsetDateTime createdAt;
       
}