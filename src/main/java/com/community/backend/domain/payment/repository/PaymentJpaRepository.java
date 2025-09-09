package com.community.backend.domain.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.payment.entity.ChargeHistory;

@Repository
public interface PaymentJpaRepository extends JpaRepository<ChargeHistory, Long> {
	Optional<ChargeHistory> findByImpUid(String impUid);
}
