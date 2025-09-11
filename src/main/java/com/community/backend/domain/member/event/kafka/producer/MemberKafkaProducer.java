package com.community.backend.domain.member.event.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.community.backend.common.kafka.dto.MessageWrapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberKafkaProducer {

	private final KafkaTemplate<String, MessageWrapper<?>> kafkaTemplate;

	public <T> void sendMessage(String topicName, MessageWrapper<T> message) {

		kafkaTemplate.send(topicName, message);
	}
}
