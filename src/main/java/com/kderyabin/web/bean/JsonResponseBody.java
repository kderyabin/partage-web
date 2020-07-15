package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class JsonResponseBody {
    Boolean error = false;
    String errMsg = "";
    String output = "";
}
