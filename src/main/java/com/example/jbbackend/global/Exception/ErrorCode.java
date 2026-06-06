package com.example.jbbackend.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COM001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COM002", "허용되지 않은 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COM003", "서버 내부 오류입니다."),
    INVALID_INPUT_FORMAT(HttpStatus.BAD_REQUEST, "COM004", "잘못된 입력 형식입니다."),
    JSON_PROCESSING_FAILED(HttpStatus.BAD_REQUEST, "COM005", "JSON 처리 중 오류가 발생했습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEM001", "해당 회원을 찾을 수 없습니다."),
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "MEM002", "이미 사용중인 이메일입니다."),
    INVALID_MEMBER_ROLE(HttpStatus.BAD_REQUEST, "MEM003", "잘못된 회원 역할입니다."),
    NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "MEM004", "이미 사용중인 닉네임입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USR001", "회원을 찾을 수 없습니다."),
    USER_ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "USR002", "이미 탈퇴한 회원입니다."),

    // Auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUT001", "인증에 실패하였습니다."),
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "AUT002", "접근 권한이 없습니다."),

    // Team
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEA001", "팀을 찾을 수 없습니다."),

    // JWT
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "JWT001", "유효하지 않은 리프레시 토큰입니다"),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "JWT002", "리프레시 토큰이 만료되었습니다"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT003", "유효하지 않은 액세스 토큰입니다"),
    MALFORMED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT004", "잘못된 액세스 토큰입니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT005", "만료된 액세스 토큰입니다"),
    UNSUPPORTED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT006", "지원되지 않는 액세스 토큰입니다."),
    ILLEGAL_ARGUMENT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT007", "유효하지 않은 액세스 토큰입니다."),

    // OAuth
    INVALID_OAUTH_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "OAU001", "유효하지 않은 OAuth 액세스 토큰입니다."),
    VERIFY_FAILED_OAUTH_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "OAU002", "OAuth 액세스 토큰 인증을 실패하였습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.UNAUTHORIZED, "OAU003", "지원되지 않는 OAuth 제공자입니다."),

    // Login
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOG001", "로그인에 실패하였습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "LOG002", "로그아웃에 실패하였습니다."),

    // Mentor
    MENTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "MEN001", "해당 멘토를 찾을 수 없습니다."),
    MENTOR_IS_ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEN002", "이미 멘토로 등록된 회원입니다."),
    INVALID_MENTOR_LEVEL(HttpStatus.BAD_REQUEST, "MEN003", "잘못된 멘토 레벨입니다."),
    INVALID_APPLY_PARAMETER(HttpStatus.BAD_REQUEST, "MEN004", "잘못된 멘토 지원 매개변수입니다."),

    // Chatting
    CHATTING_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHA001", "해당 채팅방을 찾을 수 없습니다."),
    NOT_A_PARTICIPANT(HttpStatus.FORBIDDEN, "CHA002", "채팅방에 참여하지 않았습니다."),
    INVALID_PARTICIPANT_ROLE(HttpStatus.BAD_REQUEST, "CHA003", "잘못된 참가자 역할입니다."),
    CHATTING_PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "CHA004", "해당 채팅방의 참여 정보를 찾을 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MES001", "해당 메시지를 찾을 수 없습니다."),
    NOT_ENOUGH_QUESTION_TICKET(HttpStatus.BAD_REQUEST, "MES002", "질문권이 부족합니다."),

    // Merchandise
    MERCHANDISE_NOT_FOUND(HttpStatus.NOT_FOUND, "MER001", "해당 상품을 찾을 수 없습니다."),

    // Ticket
    TICKET_IS_ALREADY_REGISTERED(HttpStatus.CONFLICT, "TIC001", "이미 등록된 payment ID 입니다."),
    TICKET_NOT_REGISTERED(HttpStatus.NOT_FOUND, "TIC002", "등록되지 않은 payment ID 입니다."),

    // Credit
    CREDIT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "CRE001", "멘토가 보유한 크레딧보다 더 많은 크레딧을 사용할 수 없습니다."),
    INVALID_CREDIT_AMOUNT(HttpStatus.BAD_REQUEST, "CRE002", "사용할 크레딧은 0보다 큰 값이어야 합니다."),

    // Withdrawal
    WITHDRAWAL_NOT_FOUND(HttpStatus.NOT_FOUND, "WIT001", "해당 출금 요청을 찾을 수 없습니다."),

    // SMTP
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "SMT001", "잘못된 인증 코드입니다."),
    EMAIL_IS_ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEN002", "이미 등록된 이메일입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "SMT003", "인증되지 않은 이메일입니다."),

    // PortOne
    // === 결제 요청 오류 (4xx) ===
    PAYMENT_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "PAY001", "잘못된 결제 요청입니다."),
    PAYMENT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "PAY002", "결제 요청에 대한 인증이 필요합니다."),
    PAYMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "PAY003", "결제 요청 권한이 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAY004", "결제 정보를 찾을 수 없습니다."),

    PAYMENT_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "PAY005", "결제가 완료되지 않았습니다."),
    PAYMENT_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "PAY006", "이미 완료된 결제입니다."),
    PAYMENT_CANCELLED(HttpStatus.BAD_REQUEST, "PAY007", "취소된 결제입니다."),
    PAYMENT_FAILED(HttpStatus.PAYMENT_REQUIRED, "PAY008", "결제에 실패했습니다."),

    // === 서버/시스템 오류 (5xx) ===
    PAYMENT_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAY101", "알 수 없는 결제 오류가 발생했습니다."),
    PAYMENT_UNRECOGNIZED_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "PAY102", "지원하지 않는 결제 응답입니다. SDK를 업데이트하세요."),
    PAYMENT_GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "PAY103", "결제 대행사(Gateway)와의 통신에 실패했습니다."),
    PAYMENT_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "PAY104", "결제 요청이 시간 초과되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
