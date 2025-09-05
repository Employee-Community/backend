package com.community.backend.domain.member.entity;

import org.hibernate.annotations.SQLRestriction;

import com.community.backend.common.entity.BaseEntity;
import com.community.backend.domain.member.enums.LogInType;
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
@SQLRestriction("state <> 2")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LogInType loginType;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name="id", nullable = false, unique = true)
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
	private Integer state;

	private Member(LogInType loginType, String name, String id, String password, String email, String phone,
		String nickname, MemberRole role, Integer state) {

		this.loginType = loginType;
		this.name = name;
		this.id = id;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.nickname = nickname;
		this.role = role;
		this.state = state;
	}

	/*
	** description : 기본 회원가입 추후 Oauth 기준으로 LoginType이 다를 것으로 인지하여 팩토리 메서드 패턴을 이용하여 분리
	 */
	public static Member createDefault(String name, String id, String password, String email, String phone,
		String nickname, MemberRole role) {

		return new Member(LogInType.DEFAULT, name, id, password, email, phone, nickname, role, 1);
	}

	public void deleteMember() {
		this.state = 2;
	}
}
