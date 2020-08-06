package com.kderyabin.web.bean;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.PersonModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Item form data
 */
@ToString
@Getter @Setter
public class Item {

    private Long id;
    @NotEmpty
    @Length(max = 50, message = "error.value_too_big")
    private String title;
    @NotNull( message = "msg.amount_is_required")
    private Float amount;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "msg.date_is_required")
    private String date;
    @NotNull(message = "msg.person_is_required")
    private Long participant;
    /**
     * Date pattern
     */
    private static String pattern = "yyyy-MM-dd";

    public Item() {
        date = convertDateToString(Date.from(Instant.now()));
    }

    /**
     * Converts BoardItemModel into Item instance.
     * @param model BoardItemModel instance.
     * @return Item instance.
     */
    public static Item getItem(BoardItemModel model){
        Item instance = new Item();
        instance.setId(model.getId());
        instance.setTitle(model.getTitle());
        instance.setAmount(model.getAmount().floatValue());
        if(model.getDate() !=null) {
            instance.setDate(convertDateToString(model.getDate()));
        }

        if(model.getPerson() != null ) {
            instance.setParticipant(model.getPerson().getId());
        }

        return instance;
    }

    /**
     * Converts data into BoardItemModel.
     * @return  BoardItemModel instance
     */
    public BoardItemModel getModel(){
        BoardItemModel model = new BoardItemModel();
        model.setId(getId());
        model.setTitle(getTitle());
        model.setAmount(getAmount().toString());
        if(date != null){
            model.setDate(convertStringToDate(date));
        }
        if(participant != null) {
            // Person model contains only the id so it's must be initialized later.
            PersonModel person = new PersonModel( participant, null);
            model.setPerson(person);
        }

        return model;
    }

    /**
     * Converts Date into into its String representation.
     * @param date Date to covert.
     * @return
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * Converts date represented with pattern into a Date instance.
     * If date can not be retrieved from the pattern the current date (today) is returned.
     * @param date String representation of the date.
     * @return Date instance
     */
    public static java.sql.Date convertStringToDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date d = simpleDateFormat.parse(date);
            return new java.sql.Date(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return new java.sql.Date(System.currentTimeMillis());
        }
    }

}
