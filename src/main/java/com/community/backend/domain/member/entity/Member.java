package com.community.backend.domain.member.entity;

import com.community.backend.common.entity.BaseEntity;
import com.community.backend.domain.member.enums.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="TBL_JOBTALK_MEMBER")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column(name = "login_type", nullable = false)
	private String loginType;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name="id", nullable = false)
	private String id;

	@Column(name="password", nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name="phone", nullable = false)
	private String phone;

	@Column(name="nickname", nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable = false)
	private MemberRole role = MemberRole.FREE;

	@Column(name = "state", nullable = false)
	private Integer state = 1;
}
