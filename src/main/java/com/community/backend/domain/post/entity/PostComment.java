package com.community.backend.domain.post.entity;

import com.community.backend.common.entity.BaseEntity;
import com.community.backend.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "TBL_JOBTALK_COMMENT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_idx", nullable = false)
	private Post post;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "state", nullable = false)
	private Integer state;

	public void updatePostComment(String content) {
		this.content = content;
	}
}
