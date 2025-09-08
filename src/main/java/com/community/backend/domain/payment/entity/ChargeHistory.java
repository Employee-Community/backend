package com.community.backend.domain.payment.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="TBL_JOBTALK_MEMBER_CHARGE_HISTORY")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargeHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_idx", nullable = false)
	private Member member;

	@Column(name = "charge_amount", nullable = false)
	private Integer amount;

	@Column(name = "payment_type", nullable = false)
	private String paymentType;

	private ChargeHistory(Member member, Integer amount, String paymentType) {
		this.member = member;
		this.amount = amount;
		this.paymentType = paymentType;
	}

	public static ChargeHistory of(Member member, Integer amount, String paymentType) {
		return new ChargeHistory(member, amount, paymentType);
	}
}
