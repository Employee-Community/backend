package com.community.backend.domain.mail.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.mail.entity.MailHistory;
import com.community.backend.domain.mail.enums.MailCode;
import com.community.backend.domain.mail.repository.MailHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailHistoryRepository repository;

    @Value("${spring.mail.username}")
    private String username;

    public MailCode sendMail(MailSendRequestDto requestDto) {
        MailCode result = MailCode.FAIL;

        // 메일 발송 성공 / 실패 리스트(DB 로그 저장을 위해)
        List<MailHistory> successList = new ArrayList<>();
        List<MailHistory> failList = new ArrayList<>();

        // 메세지 객체 생성(발송자는 같음)
        SimpleMailMessage message = new SimpleMailMessage();
        if (requestDto.getFrom() == null || requestDto.getFrom().isEmpty()) {
            message.setFrom(String.format("%s@gmail.com", username));
        } else {
            message.setFrom(requestDto.getFrom());
        }

        // 메일 발송
        for (var receiver : requestDto.getTo()) {
            try {
                if (!isValidEmail(receiver))
                    continue;

                // 추후 수신자 별 메일 제목, 내용이 달라질 경우 대비
                message.setTo(receiver);
                message.setSubject(requestDto.getSubject());
                message.setText(requestDto.getText());

                mailSender.send(message);

                result = MailCode.SUCCESS;
                successList.add(new MailHistory(requestDto.getFrom(), receiver, message.getSubject(), message.getText(),
                        result.toString()));
            } catch (Exception e) {
                result = MailCode.FAIL;
                failList.add(new MailHistory(requestDto.getFrom(), receiver, message.getSubject(), message.getText(),
                        result.toString()));
            }
        }

        // 성공 / 실패 로그 저장
        if (!successList.isEmpty())
            repository.saveAllMailHistory(successList);
        if (!failList.isEmpty())
            repository.saveAllMailHistory(failList);

        return result;
    }

    private boolean isValidEmail(String email) {
        // 이메일 유효성 검사 정규표현식
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
