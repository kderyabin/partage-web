package com.kderyabin.core.model;

import java.math.BigDecimal;

import com.kderyabin.core.services.BoardBalance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BoardPersonTotal {

    private BigDecimal total;
    private Long boardId;
    private PersonModel person;
    /**
     * Average amount for the board
     */
    private BigDecimal boardAverage;
    /**
     * Person balance
     * @see BoardBalance
     */
    private BigDecimal balance;

    public BoardPersonTotal() {
    }

    public BoardPersonTotal(BigDecimal total, Long boardId, PersonModel person) {
        this.total = total != null ? total : new BigDecimal("0") ;
        this.boardId = boardId;
        this.person = person;
    }

    public BoardPersonTotal(BigDecimal total, Long personId, String personName, Long boardId) {
        this.total = total != null ? total : new BigDecimal("0") ;
        this.boardId = boardId;
        person = new PersonModel(personId, personName);
    }

    public String getName() {
       return person.getName();
    }
}
