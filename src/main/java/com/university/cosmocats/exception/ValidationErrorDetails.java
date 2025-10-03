package com.university.cosmocats.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationErrorDetails {
    String fieldName;
    String reason;
}
