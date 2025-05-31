package com.valuelabs.exam.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.valuelabs.exam.dto.TrackingNumberRequest;
import com.valuelabs.exam.dto.TrackingNumberResponse;

@Service
public class TrackingNumberService {
    
    private static final String CACHE_NAME = "trackingNumbers";
    
    public synchronized TrackingNumberResponse generateTrackingNumber(TrackingNumberRequest request) 
            throws Exception {
        
        try {
            
            //OffsetDateTime createdAt = OffsetDateTime.parse(OffsetDateTime.now(ZoneId.systemDefault()).toString(),FORMATTER);
            String trackingNumber = generateUniqueTrackingNumber(request, request.getCreatedAt());
            
            // Store in cache
            cacheTrackingNumber(trackingNumber, request);
            
            TrackingNumberResponse response = new TrackingNumberResponse();
            response.setTrackingNumber(trackingNumber);
            response.setOriginCountryId(request.getOriginCountryId());
            response.setDestinationCountryId(request.getDestinationCountryId());
            response.setWeight(request.getWeight());
            response.setCustomerId(request.getCustomerId().toString());
            response.setCustomerName(request.getCustomerName());
            response.setCustomerSlug(request.getCustomerSlug());
            response.setCreatedAt(request.getCreatedAt());
            
            return response;
        } catch (Exception e) {
            throw new Exception("Error generating tracking number: " + e.getMessage(), e);
        }
    }
    
    public String generateUniqueTrackingNumber(TrackingNumberRequest request, OffsetDateTime createdAt) {
            String numberGenerationString = request.getOriginCountryId() + 
                              request.getDestinationCountryId() +
                              request.getCustomerId().toString() +
                              createdAt.toString();
            
            
            return UUID.nameUUIDFromBytes(numberGenerationString.getBytes()).toString();
    }
    
    @Cacheable(value = CACHE_NAME, key = "#trackingNumber")
    public String isTrackingNumberUsed(String trackingNumber) {
        return null; // Return null if not in cache
    }
    
    @CachePut(value = CACHE_NAME, key = "#trackingNumber")
    public String cacheTrackingNumber(String trackingNumber, TrackingNumberRequest request) {
		return trackingNumber; // Store in cache
	}
}
