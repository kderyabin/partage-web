package com.kderyabin.web.storage.repository;

import com.kderyabin.web.storage.entity.MailActionEntity;
import com.kderyabin.web.storage.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO for mail table
 */
@Repository
public interface MailActionRepository extends JpaRepository<MailActionEntity, Long> {
    /**
     * Fetch a record by token
     * @param token Token to fetch
     * @return      MailActionEntity instance or null if no record with given token exists in database
     */
    MailActionEntity findByToken( String token);

    /**
     * Fetches a record by user
     * @param user UserEntity instance
     * @return     MailActionEntity instance or null if no record with given token exists in database
     */
    MailActionEntity findByUser(UserEntity user);
}
