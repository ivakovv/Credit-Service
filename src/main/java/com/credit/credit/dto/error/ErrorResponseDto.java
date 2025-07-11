package com.credit.credit.dto.error;

public record ErrorResponseDto(int status, String error, String message) {
}
