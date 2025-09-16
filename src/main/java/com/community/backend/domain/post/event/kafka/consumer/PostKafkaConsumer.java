package com.community.backend.domain.post.event.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.community.backend.common.kafka.GroupId;
import com.community.backend.common.kafka.TopicNames;
import com.community.backend.common.kafka.dto.MessageWrapper;
import com.community.backend.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostKafkaConsumer {

	private final PostRepository postRepository;

	@Transactional
	@KafkaListener(topics = TopicNames.MEMBERS_DELETE_TOPIC, groupId = GroupId.MEMBER_GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
	public void handleMemberDelete(@Payload MessageWrapper<Long> wrapper){

		postRepository.deleteAllPosts(wrapper.data());
	}
}
