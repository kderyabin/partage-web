package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Getter @Setter
public class Board {

    private Long id;

    @Size(min = 1, max = 50, message = "msg.board_name_required")
    private String name;

    private String description;

    @NotEmpty(message = "error.currency_is_required")
    private String currency;

    @Size( min = 1, message = "msg.provide_participants")
    private List<String> participants;
}
