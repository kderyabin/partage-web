package com.kderyabin.web.storage.repository;

import com.kderyabin.web.storage.entity.MailActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailActionRepository extends JpaRepository<MailActionEntity, Long> {
    /**
     * Fetch a record by token
     * @param token
     * @return
     */
    public MailActionEntity findByToken( String token);
}
