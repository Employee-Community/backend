package com.community.backend.domain.post.entity;

import com.community.backend.common.entity.BaseEntity;
import com.community.backend.domain.postcategory.entity.PostCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TBL_JOBTALK_POST")
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column(name = "member_idx", nullable = false)
	private Long memberIdx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_idx", nullable = false)
	private PostCategory category;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "contents", nullable = false)
	private String contents;

	@Column(name = "view_count")
	private Integer viewCount = 0;

	@Column(name = "state", nullable = false)
	private Integer state = 1;
}
