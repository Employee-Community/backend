package com.community.backend.domain.mail.repository;

import java.util.List;

import com.community.backend.domain.mail.entity.MailHistory;

public interface MailHistoryRepository {

    public void saveMailHistory(MailHistory mailHistory);

    public void saveAllMailHistory (List<MailHistory> mailHistoryList);
}
