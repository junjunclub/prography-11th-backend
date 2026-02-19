package com.prography.backend_assignment.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ErrorDetail error;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorDetail {
        private String code;
        private String message;
    }
}
