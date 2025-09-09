package com.community.backend.domain.mail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailSendRequestDto {

    @NotEmpty(message = "수신자 이메일은 필수입니다.")
    private String[] to;

    private String from;

    @NotBlank(message = "제목은 필수입니다.")
    private String subject;

    @NotBlank(message = "내용은 필수입니다.")
    private String text;
}
