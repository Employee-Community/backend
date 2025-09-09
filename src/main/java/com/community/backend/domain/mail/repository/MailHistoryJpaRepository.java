package com.community.backend.domain.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.mail.entity.MailHistory;

@Repository
public interface MailHistoryJpaRepository extends JpaRepository<MailHistory, Long> {

}
