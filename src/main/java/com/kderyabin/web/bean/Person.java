package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@ToString
@Getter @Setter
public class Person {
    @Size(min = 1, max = 50, message = "msg.participant_name_min_max")
    private String name;
}
