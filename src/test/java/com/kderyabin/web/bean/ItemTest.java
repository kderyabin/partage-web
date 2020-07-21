package com.kderyabin.web.bean;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {

    final private Logger LOG = LoggerFactory.getLogger(ItemTest.class);

    @Test
    void getFromModel() {
        BoardItemModel model = new BoardItemModel();
        model.setId(1L);
        model.setTitle("Test title");
        model.setAmount("152.39");
        LocalDate localDate = LocalDate.of(2020, 7, 20);
        model.setDate(Date.valueOf(localDate));

        PersonModel person = new PersonModel( 1L, "Jane");

        model.setPerson(person);

        Item item = Item.getItem(model);
        assertEquals(1L, (long) item.getId());
        assertEquals(model.getTitle(), item.getTitle());
        assertEquals(152.39f, item.getAmount());
        assertEquals("20/07/2020", item.getDate());
        assertEquals(1L, (long)item.getParticipant());
    }
}