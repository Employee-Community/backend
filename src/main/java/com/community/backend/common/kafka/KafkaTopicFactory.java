package com.community.backend.common.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicFactory {

	private static final int DEFAULT_PARTITIONS = 3;
	private static final int DEFAULT_REPLICAS = 1;
	private static final long DEFAULT_RETENTION_MS = 7 * 24 * 60 * 60 * 1000L;

	public NewTopic createTopic(String name, int partitions, int replicas, long retentionMs) {

		return TopicBuilder
			.name(name)
			.partitions(partitions)
			.replicas(replicas)
			.config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(retentionMs))
			.build();
	}

	public NewTopic createTopic(String name) {

		return createTopic(name, DEFAULT_PARTITIONS, DEFAULT_REPLICAS, DEFAULT_RETENTION_MS);
	}
}
