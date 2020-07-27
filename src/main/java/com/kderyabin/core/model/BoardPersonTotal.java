package com.kderyabin.core.model;

import com.kderyabin.core.services.BoardBalance;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * BoardPersonTotal model contains person's expenses data for the given board.
 */
@ToString
@Getter
@Setter
public class BoardPersonTotal {
    /**
     * Board ID.
     */
    private Long boardId;
    /**
     * Person instance
     */
    private PersonModel person;
    /**
     * Total amount spent by the person for the board with given {@link #boardId}.
     */
    private BigDecimal total;
    /**
     * Average amount spent by all participants for the given board
     */
    private BigDecimal boardAverage;
    /**
     * Person balance
     *
     * @see BoardBalance#getBalancePerPerson() for explanations on balance.
     */
    private BigDecimal balance;

    public BoardPersonTotal() {
    }

    public BoardPersonTotal(BigDecimal total, Long boardId, PersonModel person) {
        this.total = total != null ? total : new BigDecimal("0");
        this.boardId = boardId;
        this.person = person;
    }

    public BoardPersonTotal(BigDecimal total, Long personId, String personName, Long boardId) {
        this.total = total != null ? total : new BigDecimal("0");
        this.boardId = boardId;
        person = new PersonModel(personId, personName);
    }

    public String getName() {
        return person.getName();
    }
}
