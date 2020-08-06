package com.kderyabin.core.services;


import com.kderyabin.core.model.BoardPersonTotal;
import com.kderyabin.core.model.PersonModel;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Calculates statistics on board expenses: average amount, spent, overpaid amount and amount of debt to share.
 * For participants balance see {@link #getBalancePerPerson()}
 * For the amount of debt to split between participants see {@link #shareBoardTotal()}
 * Usage:
 * BoardBalance boardBalance = BoardBalance.forBoard(storageManager.getBoardPersonTotal(boardId));
 */
@ToString
public class BoardBalance {

    final private Logger LOG = LoggerFactory.getLogger(BoardBalance.class);

    /**
     * List of totals per participants
     */
    private List<BoardPersonTotal> totals = new ArrayList<>();

    /**
     * Total amount of the board.
     */
    private BigDecimal total;
    /**
     * Average amount of the board derived from total amount.
     */
    private BigDecimal average;

    /**
     * Map containing a list of participants and amount owed to any other participant
     * The key of the map is a participant (PersonModel) and a value is another map
     * where the key is a participant (PersonModel) to whom money are owned and the value is an amount.
     */
    private Map<PersonModel, Map<PersonModel, BigDecimal>> share = new HashMap<>();

    /**
     * BoardBalance builder
     * @param totals
     * @return
     */
    public static BoardBalance buildFor(List<BoardPersonTotal> totals ) {
        BoardBalance  instance = new BoardBalance();
        instance.setTotals(totals);
        instance.init();
        instance.shareBoardTotal();

        return instance;
    }

    /**
     * Calculates a total amount of the board.
     */
    public void calculateTotal() {
        total = totals.stream().map(BoardPersonTotal::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates average amount of the board.
     * We loose 1 cent in some cases.
     */
    public void calculateAverage() {
        average = total.divide(BigDecimal.valueOf(totals.size()), 2, RoundingMode.FLOOR);
    }

    /**
     * Pushes board average and total into this.totals entries
     */
    private void populateParticipants() {
        for (BoardPersonTotal person : totals) {
            BigDecimal balance = person.getTotal().subtract(average);
            person.setBalance(balance);
            person.setBoardAverage(average);
        }
    }

    /**
     * Checks if balance is empty.
     * A balance is considered empty if its total amount is 0 or null.
     * @return TRUE if empty FALSE otherwise
     */
    public boolean isEmpty() {
        return total == null || total.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Calculates all required data.
     * This method must be called any time the {@link BoardBalance#setTotals(List)} method is used.
     */
    public void init() {
        if (totals.size() > 0) {
            calculateTotal();
            calculateAverage();
            if (!isEmpty()) {
                populateParticipants();
            }
        }
    }

    /**
     * Returns a map with a Person as a key and amount of the balance as a value.
     * Negative balance means the person owns money.
     * Positive balance means the person payed too much.
     * @return Map of all persons with their balance
     */
    public Map<PersonModel, BigDecimal> getBalancePerPerson() {

        Map<PersonModel, BigDecimal> balances = new LinkedHashMap<>();
        totals.stream().parallel().forEach(person -> balances.put(person.getPerson(), person.getBalance()));
        return balances;
    }

    /**
     * Calculate amount of debt to share.
     * Produces  2 dimensional array where column header represents a creditor
     * and row header represents a debtor.
     * Ex.: given the balance for total amount 180 with average 60: Jane=50, Anna=-60, John=10
     * <p>
     * |        |   Jane    |   Anna    |   John    |
     * | Jane   |   0       |   0       |   0       |
     * | Anna   |   50      |   0       |   10      |
     * | John   |   0       |   0       |   0       |
     * Ann owes 50 to Jane and 10 to John
     * </p>
     */
    public void shareBoardTotal() {
        Map<PersonModel, BigDecimal> balances = getBalancePerPerson();
        LOG.debug(">>> balances: " + balances.toString());
        if (isEmpty()) {
            return;
        }
        // share amounts between participants
        for (BoardPersonTotal person : totals) {

            BigDecimal personBalance = balances.get(person.getPerson());
            // Negative balance means person owns money.
            // So we are going to dispatch owed amount between participants having payed too much.
            Map<PersonModel, BigDecimal> line = new HashMap<>();
            share.put(person.getPerson(), line);
            for (BoardPersonTotal otherParticipant : totals) {
                PersonModel friend = otherParticipant.getPerson();
                // skip if current person does not owes money or it's the same person
                if (personBalance.compareTo(BigDecimal.ZERO) >= 0 || friend.equals(person.getPerson())) {
                    line.put(friend, new BigDecimal("0"));
                    continue;
                }
                BigDecimal friendBalance = balances.get(friend);
                // Does the otherParticipant need to be payed back?
                if (friendBalance.compareTo(BigDecimal.ZERO) > 0) {
                    // Yes. Needs to be refunded.
                    LOG.debug("Creditor: " + friendBalance + " Debtor: " + personBalance + "(" + personBalance.abs() + ")");
                    if (friendBalance.compareTo(personBalance.abs()) > 0) {
                        LOG.debug("Debt can be refunded partially");
                        // The debt is less then amount payed by the otherParticipant so it can be sold partially
                        // Ex.: 60 + (-20) = 40
                        line.put(friend, personBalance.abs());
                        // update otherParticipant's balance
                        balances.put(friend, friendBalance.add(personBalance));
                        personBalance = new BigDecimal("0");
                    } else {
                        LOG.debug("Debt can be refunded totally");
                        // The debt is greater then amount payed by the otherParticipant and can be sold totally.
                        // Ex.: 10 + (-30)
                        line.put(friend, friendBalance);
                        // update balance
                        personBalance = friendBalance.add(personBalance);
                        balances.put(friend, new BigDecimal("0"));
                    }
                } else {
                    line.put(friend, new BigDecimal("0"));
                }
            }
            // update the balance of the current person
            balances.put(person.getPerson(), personBalance);

        }
        LOG.debug(">>> balances end" + balances.toString());
    }

    public List<BoardPersonTotal> getTotals() {
        return totals;
    }

    public void setTotals(List<BoardPersonTotal> totals) {
        this.totals = totals;
    }

    public Map<PersonModel, Map<PersonModel, BigDecimal>> getShare() {
        return share;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getAverage() {
        return average;
    }
}
