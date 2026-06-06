package com.example.jbbackend.domain.ping;

import com.example.jbbackend.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {

    // 단순 텍스트 응답 ("pong")
    @GetMapping("/api/v1/ping")
    public ResponseEntity<ApiResponse<String>> pingText() {
        return ResponseEntity.ok(
            ApiResponse.success(null, HttpStatus.OK, "pong")
        );
    }

    // JSON 형태 응답 (상태 체크용)
    @GetMapping("/api/v1/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "UP");
        statusData.put("message", "Spring Boot Server is running flawlessly!");

        return ResponseEntity.ok(
            ApiResponse.success(statusData)
        );
    }
}