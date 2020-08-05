package com.kderyabin.core.services;

import com.kderyabin.core.model.BoardPersonTotal;
import com.kderyabin.core.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BoardBalanceTest {
    final private Logger LOG = LoggerFactory.getLogger(BoardBalanceTest.class);

    @Test
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
}