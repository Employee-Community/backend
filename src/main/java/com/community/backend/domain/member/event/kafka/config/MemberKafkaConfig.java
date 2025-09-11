package com.community.backend.domain.member.event.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.community.backend.common.kafka.KafkaTopicFactory;
import com.community.backend.common.kafka.TopicNames;

@Configuration
public class MemberKafkaConfig {

	private final KafkaTopicFactory kafkaTopicFactory;

	public MemberKafkaConfig(KafkaTopicFactory kafkaTopicFactory) {
		this.kafkaTopicFactory = kafkaTopicFactory;
	}

	@Bean
	public NewTopic memberDeleteTopic() {

		return kafkaTopicFactory.createTopic(TopicNames.MEMBERS_DELETE_TOPIC);
	}
}
