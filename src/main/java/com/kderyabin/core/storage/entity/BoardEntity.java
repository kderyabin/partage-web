package com.kderyabin.core.storage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Sharing table mapping.
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "board")
@NamedNativeQuery(
        name = "BoardEntity.loadRecent",
        query = "select b.* from board b order by b.updated desc limit ?1",
        resultClass = BoardEntity.class
)
public class BoardEntity {
    /**
     * Board Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    /**
     * Board name.
     */
    @Column(name = "board_name", nullable = false, length = 50)
    private String name;

    /**
     * Description of the board.
     */
    @Column(name = "description")
    private String description;

    /**
     * Currency for the board's items.
     */
    @Column(name = "currency", length = 3)
    private String currency;

    /**
     * Board's creation time.
     */
    @Column(name = "creation", nullable = false)
    private Timestamp creation = new Timestamp(System.currentTimeMillis());

    /**
     * Board's update time.
     */
    @Column(name = "updated", nullable = false)
    private Timestamp update = new Timestamp(System.currentTimeMillis());

    /**
     * Participants of the board.
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "board_person",
            joinColumns = {@JoinColumn(name = "board_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private Set<PersonEntity> participants = new LinkedHashSet<>();

    /**
     * Board's items (expenses).
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<BoardItemEntity> items = new LinkedHashSet<>();

    public BoardEntity() {
    }

    public BoardEntity(String name) {
        this.name = name;
    }

    /**
     * Adds person to participants list.
     *
     * @param participant PersonEntity instance
     * @return Status of the operation
     */
    public boolean addParticipant(PersonEntity participant) {
        return participants.add(participant);
    }

    /**
     * Removes person from participants list.
     *
     * @param participant PersonEntity instance
     * @return Status of the operation
     */
    public boolean removeParticipant(PersonEntity participant) {
        return participants.remove(participant);
    }

    /**
     * Adds item (expense) to items list.
     *
     * @param item BoardItemEntity instance
     */
    public void addItem(BoardItemEntity item) {
        item.setBoard(this);
        items.add(item);
    }

    /**
     * Removes item (expense) from items list
     *
     * @param item Item to remove
     */
    public void removeItem(BoardItemEntity item) {
        item.setBoard(null);
        items.remove(item);
    }

    /**
     * Empties items list.
     */
    public void removeAllItems() {
        if (null == items) {
            return;
        }
        items.forEach(item -> {
            item.setBoard(null);
            item.setPerson(null);
        });
        items.clear();
    }

    /**
     * Calculates current timestamp.
     *
     * @return Current timestamp.
     */
    private Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Resets the update time for the board.
     */
    public void initUpdateTime() {
        update = getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity that = (BoardEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creation, that.creation) &&
                Objects.equals(update, that.update) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creation, update);
    }

}
