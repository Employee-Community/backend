package com.community.backend.domain.mail.service;

import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.mail.enums.MailCode;

public interface MailService {

    void sendMailAsync(MailSendRequestDto requestDto);

    MailCode sendMail(MailSendRequestDto requestDto);
}
