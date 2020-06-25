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

@ToString
@Entity
@Table(name = "board")
@NamedNativeQuery(
        name="BoardEntity.loadRecent",
        query = "select b.* from board b order by b.update desc limit ?1",
        resultClass = BoardEntity.class
)
public class BoardEntity {
	/**
	 * Board unique Id.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private Long id;
    
    /**
     * Board name.
     */
    @Getter @Setter
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    /**
     * Description of the board.
     */
    @Getter @Setter
    @Column(name = "description")
    private String description;

    /**
     * Currency for the board's items.
     */
    @Getter @Setter
    @Column(name = "currency", length = 3)
    private String currency;
    
    /**
     * Board's creation time.
     */
    @Getter @Setter
    @Column(name = "creation", nullable = false)
    private Timestamp creation = new Timestamp(System.currentTimeMillis());
    
    /**
     * Board's update time.
     */
    @Getter @Setter
    @Column(name = "update", nullable = false)
    private Timestamp update = new Timestamp(System.currentTimeMillis());
    
    /**
     * Participants of the board.
     */
    @Getter @Setter
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable( name="board_person",
                joinColumns = { @JoinColumn( name = "boardId")},
                inverseJoinColumns = { @JoinColumn( name = "personId")})
    private Set<PersonEntity> participants = new LinkedHashSet<>();
    
    /**
     * Board's items (expenses).
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<BoardItemEntity> items = new LinkedHashSet<>();

    public BoardEntity() {
    }

    public BoardEntity(String name) {
        this.name = name;
    }

    public boolean addParticipant(PersonEntity participant){
        //participant.addBoard(this);
        return participants.add(participant);
    }
    public boolean removeParticipant(PersonEntity participant){
        //participant.removeBoard(this);
        return participants.remove(participant);
    }

    public void addItem(BoardItemEntity item){
        item.setBoard(this);
        items.add(item);
    }

    public void removeItem(BoardItemEntity item){
        item.setBoard(null);
        items.remove(item);
    }
    public void removeAllItems(){
        if(null == items) {
            return;
        }
        items.forEach(item -> {
            item.setBoard(null);
            item.setPerson(null);
        });
        items.clear();
    }

    /**
     * Returns current timestamp.
     * @return Current timestamp.
     */
    private Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Resets the update time for the board.
     */
    public void initUpdateTime(){
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
