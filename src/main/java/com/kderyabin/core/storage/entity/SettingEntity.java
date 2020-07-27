package com.kderyabin.core.storage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.Objects;

/**
 * Table with user application settings.
 */
@ToString
@Getter @Setter
@Entity
@Table(name = "setting")
public class SettingEntity {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id", nullable = false)
    private Long id;
    /**
     * Setting's name
     */
    @Column(name="setting_name", length = 30, nullable = false, unique = true)
    private String name;

    /**
     * Setting's value
     */
    @Column(name="setting_value")
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingEntity)) return false;
        SettingEntity that = (SettingEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }
}
