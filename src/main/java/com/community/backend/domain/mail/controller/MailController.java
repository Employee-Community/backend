package com.community.backend.domain.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.mail.enums.MailCode;
import com.community.backend.domain.mail.service.MailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<MailCode>> sendMail(@Valid @RequestBody MailSendRequestDto request) {
        try {
            MailCode code = mailService.sendMail(request);
            return ResponseEntity.ok(ApiResponse.success("ok", code));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.success("ok", MailCode.FAIL));
        }
    }
}
