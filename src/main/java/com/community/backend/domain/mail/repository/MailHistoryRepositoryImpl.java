package com.community.backend.domain.mail.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.community.backend.domain.mail.entity.MailHistory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MailHistoryRepositoryImpl implements MailHistoryRepository {

    private final MailHistoryJpaRepository jpaRepository;

    @Override
    public void saveMailHistory(MailHistory mailHistory) {
        jpaRepository.save(mailHistory);
    }

    @Override
    public void saveAllMailHistory(List<MailHistory> mailHistoryList) {
        jpaRepository.saveAll(mailHistoryList);
    }
}