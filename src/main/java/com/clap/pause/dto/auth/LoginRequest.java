package com.clap.pause.dto.auth;

import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Pattern(regexp = "^(010|011|016|017|018|019)-([0-9]{3,4})-([0-9]{4})", message = "허용되지 않은 형식의 연락처입니다.")
        String phoneNumber,
        @Pattern(regexp = "^[0-9a-zA-Z\\-\\_\\+\\!\\*\\@\\#\\$\\%\\^\\&\\(\\)\\.]{8,}$", message = "허용되지 않은 형식의 패스워드입니다.")
        String password
) {
}
