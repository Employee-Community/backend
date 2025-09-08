package com.community.backend.domain.mail.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.mail.enums.MailCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public MailCode sendMail(MailSendRequestDto requestDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(requestDto.getTo());
            message.setSubject(requestDto.getSubject());
            message.setText(requestDto.getText());
            mailSender.send(message);

            return MailCode.SUCCESS;
        } catch (Exception e) {
            return MailCode.SEND_FAILED;
        }
    }
}
