package com.kderyabin.core.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model derived from {@link com.kderyabin.core.storage.entity.BoardItemEntity}
 * @see com.kderyabin.core.storage.entity.BoardItemEntity for detailed description of class fields.
 */
@ToString
@Getter
@Setter
public class BoardItemModel {
    private Long id;
    private String title;
    private BigDecimal amount;
    private Date date = new Date(System.currentTimeMillis());
    private PersonModel person;
    private BoardModel board;

    public BoardItemModel() {
    }

    public BoardItemModel(String title) {
        this.title = title;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        this.amount = new BigDecimal(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardItemModel)) return false;
        BoardItemModel that = (BoardItemModel) o;
        return  Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date);
    }
}

