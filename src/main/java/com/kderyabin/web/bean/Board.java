package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Board data submitted with a form.
 * Participants list is validates apart.
 */
@ToString
@Getter @Setter
public class Board {

    @Size(min = 1, max = 50, message = "msg.board_name_required")
    private String name;

    private String description;

    @NotEmpty(message = "error.currency_is_required")
    private String currency;
}
