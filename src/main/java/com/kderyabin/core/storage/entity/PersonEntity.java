package com.kderyabin.core.storage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Table(name = "person")
@NamedNativeQuery(
        name = "PersonEntity.findAllByBoardId",
        query = "select p.* from person p inner join board_person bp on bp.person_id = p.person_id where bp.board_id = ?1",
        resultClass = PersonEntity.class)
public class PersonEntity {
	@Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", nullable = false)
    private Long id;
	
	@Getter @Setter
    @Column(name = "person_name", nullable = false, length = 50)
    private String name;
	
	@Getter @Setter
    @ToString.Exclude
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "participants")
    private Set<BoardEntity> boards = new LinkedHashSet<>();
	
	@Getter @Setter
    @ToString.Exclude
    @OneToMany(mappedBy = "person", cascade =  {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<BoardItemEntity> items = new LinkedHashSet<>();

    public PersonEntity() {
    }

    public PersonEntity(String name) {
        this.name = name;
    }

    public boolean addBoard(BoardEntity boardModel) {
        return boards.add(boardModel);
    }

    public boolean removeBoard(BoardEntity boardModel) {
        return boards.remove(boardModel);
    }

    public void addItem(BoardItemEntity item){
        items.add(item);
    }
    public void removeItem(BoardItemEntity item){
        items.remove(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
