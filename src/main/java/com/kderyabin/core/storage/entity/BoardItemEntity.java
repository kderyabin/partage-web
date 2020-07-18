package com.kderyabin.core.storage.entity;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Table(name = "item")
@NamedQuery(
        name = "BoardItemEntity.findAllByBoardId",
        query = "select i from BoardItemEntity i where board_id = ?1")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "BoardItemEntity.getBoardPersonTotal",
                query = "select " +
                        "SUM(i.amount) as total, " +
                        "p.id as personId, " +
                        "p.name as personName," +
                        "bp.boardid as boardId  " +
                        "from board_person as bp  " +
                        "join person AS p on bp.personid = p.id " +
                        "left join item as i on i.person_id = p.id and i.board_id = bp.boardid " +
                        "where bp.boardid = ?1 " +
                        "group by p.id " +
                        "order by personName"
        ),
        @NamedNativeQuery(
                name = "BoardItemEntity.removeByBoardAndPerson",
                query = "delete from item where board_id = ?1 and person_id = ?2"
        )
}
)

public class BoardItemEntity {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(name = "item_title", nullable = false, length = 50)
    private String title;

    @Getter
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Getter
    @Setter
    @Column(name = "pay_date", nullable = false)
    private Date date = new Date(System.currentTimeMillis());

    @ToString.Exclude
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_person_id"))
    private PersonEntity person;

    @ToString.Exclude
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_board_id"))
    private BoardEntity board;

    public BoardItemEntity() {
    }

    public BoardItemEntity(String title) {
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
        if (!(o instanceof BoardItemEntity)) return false;
        BoardItemEntity that = (BoardItemEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date);
    }
}
