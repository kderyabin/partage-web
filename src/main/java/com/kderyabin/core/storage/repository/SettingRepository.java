package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO for settings table.
 */
@Repository
public interface SettingRepository extends JpaRepository<SettingEntity, Long> {
}
