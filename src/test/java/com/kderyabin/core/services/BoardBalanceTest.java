package com.kderyabin.core.services;

import com.kderyabin.core.model.BoardPersonTotal;
import com.kderyabin.core.model.PersonModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BoardBalanceTest {
    final private Logger LOG = LoggerFactory.getLogger(BoardBalanceTest.class);

    @Test
    @DisplayName("Should initialize data")
    void dataInitialization() {
        List<BoardPersonTotal> totals = new ArrayList<>();
        totals.add(new BoardPersonTotal(new BigDecimal("5"), 1L, new PersonModel("John")));
        totals.add(new BoardPersonTotal(new BigDecimal("2"), 1L, new PersonModel("James")));
        totals.add(new BoardPersonTotal(new BigDecimal("3"), 1L, new PersonModel("Chris")));

        BoardBalance balance = new BoardBalance();
        assertTrue(balance.isEmpty());

        balance.setTotals(totals);
        balance.init();

        LOG.debug(balance.toString());

        assertFalse(balance.isEmpty());
        assertEquals(new BigDecimal("10"), balance.getTotal());
        assertEquals(new BigDecimal("3.33"), balance.getAverage());
    }

    @Test
    @DisplayName("Should calculate refundment to 1 participant of 4")
    void shareBoardTotalSeries1() {
        // Test data
        PersonModel person1 = new PersonModel(1L,"Traveller1");
        PersonModel person2 = new PersonModel(2L,"Traveller2");
        PersonModel person3 = new PersonModel(3L,"Traveller3");
        PersonModel person4 = new PersonModel(4L,"Traveller4");

        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("100"),1L, person1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("50"),1L, person2);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("0"),1L, person3);
        BoardPersonTotal traveller4 = new BoardPersonTotal(new BigDecimal("300"),1L, person4);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);
        data.add(traveller4);
        BoardBalance balance = BoardBalance.buildFor(data);
        balance.setTotals(data);
        balance.shareBoardTotal();
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        LOG.debug(result.toString());

        assertEquals(0,result.get(person1).get(person4).compareTo(new BigDecimal("12.50")));
        assertEquals(0,result.get(person2).get(person4).compareTo(new BigDecimal("62.50")));
        assertEquals(0,result.get(person3).get(person4).compareTo(new BigDecimal("112.50")));
    }

    @Test
    @DisplayName("Should calculate refundement to 2 particpants of 3")
    void shareBoardTotalSeries2() {
        // Test data
        PersonModel person1 = new PersonModel(1L,"Traveller1");
        PersonModel person2 = new PersonModel(2L,"Traveller2");
        PersonModel person3 = new PersonModel(3L,"Traveller3");

        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("110"),1L, person1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("70"),1L, person2);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("0"),1L, person3);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);

        BoardBalance balance = BoardBalance.buildFor(data);
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        // Person 1 Total amount owed to other
        Optional<BigDecimal> sumPerson1 = result.get(person1).values().stream().reduce(BigDecimal::add);
        // Person 2 Total amount owed to other
        Optional<BigDecimal> sumPerson2 = result.get(person2).values().stream().reduce(BigDecimal::add);
        // Person 3 Total amount owed to other
        Optional<BigDecimal> sumPerson3 = result.get(person3).values().stream().reduce(BigDecimal::add);

        assertEquals(0,result.get(person3).get(person1).compareTo(new BigDecimal("50")));
        assertEquals(0,result.get(person3).get(person2).compareTo(new BigDecimal("10")));
        assertEquals(new BigDecimal("0"), sumPerson1.get());
        assertEquals(new BigDecimal("0"), sumPerson2.get());
        assertEquals(new BigDecimal("60.00" ), sumPerson3.get());
    }

    private void printArrays(Map<PersonModel, Map<PersonModel, BigDecimal>> result){
        result.forEach( (debtor, debts ) -> {
            System.out.print(debtor.getName() + " owes to\t");
            debts.forEach( (creditor, amount) -> {
                System.out.print("|\t" + creditor.getName() + " ( "+ amount+" )\t" );
            });
            System.out.println("|");
        });
    }
}