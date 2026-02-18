package com.prography.backend_assignment.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(200, "SUCCESS", "요청이 성공적으로 처리되었습니다.", null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "CREATED", "리소스가 성공적으로 생성되었습니다.", data);
    }

    public static ApiResponse<Void> error(int status, String code, String message) {
        return new ApiResponse<>(status, code, message, null);
    }
}
