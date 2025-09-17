package com.community.backend.domain.mail.event.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.community.backend.common.kafka.GroupId;
import com.community.backend.common.kafka.TopicNames;
import com.community.backend.common.kafka.dto.MessageWrapper;
import com.community.backend.common.kafka.dto.memberDto.MemberRequestDto;
import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailKafkaConsumer {

	private final MailService mailService;

	@KafkaListener(topics = TopicNames.MEMBERS_DELETE_TOPIC, groupId = GroupId.MAIL_GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
	public void handleMemberDelete(MessageWrapper<MemberRequestDto> wrapper) {

		MailSendRequestDto mailDto = new MailSendRequestDto();
		mailDto.setTo(new String[]{wrapper.data().email()});
		mailDto.setSubject("[JobTalk - 회원 삭제 알림입니다]");
		mailDto.setText("JobTalk - 회원 삭제 알림입니다.");

		mailService.sendMail(mailDto);
	}
}
