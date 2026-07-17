package com.guowei.ordering.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancelOrderDTO {

    @Size(max = 255, message = "cancel reason must not exceed 255 characters")
    private String cancelReason;
}
