package com.clap.pause.dto.auth;

import com.clap.pause.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record RegisterRequest(
        @Length(min = 1, max = 8, message = "이름은 최소 1글자, 최대 8글자까지 가능합니다.")
        String name,
        @Pattern(regexp = "^[0-9a-z\\-\\_\\+\\w]*@([0-9a-z]+\\.)+[a-z]{2,9}", message = "허용되지 않은 형식의 이메일입니다.")
        String email,
        @Pattern(regexp = "^[0-9a-zA-Z\\-\\_\\+\\!\\*\\@\\#\\$\\%\\^\\&\\(\\)\\.]{8,}$", message = "허용되지 않은 형식의 패스워드입니다.")
        String password,
        @NotBlank(message = "프로필 이미지는 반드시 입력되어야 합니다.")
        String profileImage,
        @Past(message = "유효하지 않은 생일입니다.")
        LocalDate birth,
        @NotNull(message = "성별은 반드시 입력되어야 합니다.")
        Gender gender,
        @NotBlank(message = "직업은 반드시 입력되어야 합니다.")
        String job
) {
}
