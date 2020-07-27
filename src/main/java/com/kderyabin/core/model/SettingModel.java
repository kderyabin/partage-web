package com.kderyabin.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 *  Settings model derived from {@link com.kderyabin.core.storage.entity.SettingEntity}
 * @see com.kderyabin.core.storage.entity.SettingEntity for detailed description of class fields.
 */
@ToString
@Getter @Setter
public class SettingModel {

    private Long id;
    private String name;
    private String value;

    public SettingModel() {
    }

    public SettingModel(Long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public SettingModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingModel)) return false;
        SettingModel that = (SettingModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }
}
