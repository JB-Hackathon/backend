package com.example.jbbackend.global.Exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 클래스의 message 필드에 메시지를 저장
        this.errorCode = errorCode;
    }

    // 필요 시, 기본 메시지 외에 추가적인 메시지를 전달하고 싶을 때 사용
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}