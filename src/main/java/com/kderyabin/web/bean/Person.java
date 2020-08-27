package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString
@Getter @Setter
public class Person implements Serializable {
    private static final long serialVersionUID = -4065157834050242996L;
    @Size(min = 1, max = 50, message = "msg.participant_name_min_max")
    private String name;
}
