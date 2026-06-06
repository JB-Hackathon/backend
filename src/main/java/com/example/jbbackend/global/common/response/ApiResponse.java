package com.example.jbbackend.global.common.response;

import lombok.Getter;
import com.example.jbbackend.global.Exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
//    private final String status;
//    private final String message;
//    private final T data;

//    public static ResponseEntity<> success() {
//
//    }
//
//    public static ResponseEntity<> failure() {
//
//    }


    private final int status; // HTTP 상태 코드
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // code 필드는 비어있으면 json에 포함되지 않음
    private final String code; // 커스텀 에러 코드

    @JsonInclude(JsonInclude.Include.NON_NULL) // data 필드는 비어있으면 json에 포함되지 않음
    private final T data;


    // 성공 시 생성자
    private ApiResponse() {
        this.status = HttpStatus.OK.value();
        this.message = "요청에 성공하였습니다.";
        this.code = null;
        this.data = null;
    }

    // 성공 시 생성자
    private ApiResponse(T data) {
        this.status = HttpStatus.OK.value();
        this.message = "요청에 성공하였습니다.";
        this.code = null;
        this.data = data;
    }

    // 성공 시 생성자
    private ApiResponse(T data, HttpStatus status) {
        this.status = status.value();
        this.message = "요청에 성공하였습니다.";
        this.code = null;
        this.data = data;
    }

    // 성공 시 생성자
    private ApiResponse(T data, HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.code = null;
        this.data = data;
    }

    // 실패 시 생성자
    private ApiResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.data = null;
    }

    // 실패 시 생성자 (커스텀 메시지)
    private ApiResponse(ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus().value();
        this.message = message;
        this.code = errorCode.getCode();
        this.data = null;
    }

    // 성공 응답 (데이터 미포함)
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>();
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return new ApiResponse<>(data, status);
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(T data, HttpStatus status, String message) {
        return new ApiResponse<>(data, status, message);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode);
    }

    // 실패 응답 (커스텀 메시지)
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode, message);
    }
}