package com.community.backend.domain.post.entity;

import com.community.backend.common.entity.BaseEntity;
import com.community.backend.domain.category.entity.Category;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "TBL_JOBTALK_POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column(name="name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_idx", nullable = false)
	private Category category;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "contents", nullable = false, columnDefinition = "TEXT")
	private String contents;

	@Column(name = "view_count")
	private Integer viewCount = 0;

	@Column(name = "state", nullable = false)
	private Integer state = 1;
}
