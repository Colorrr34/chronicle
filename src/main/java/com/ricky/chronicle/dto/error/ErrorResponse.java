package com.ricky.chronicle.dto.error;

public record ErrorResponse(
    int statusCode,
    String message
) {
    
}
