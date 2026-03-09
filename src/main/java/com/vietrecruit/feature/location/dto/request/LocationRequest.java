package com.vietrecruit.feature.location.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {

    @NotBlank(message = "Location name is required")
    @Size(max = 255, message = "Location name must not exceed 255 characters")
    private String name;

    private String address;
}
