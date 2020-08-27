package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter @Setter
public class JsonResponseBody implements Serializable {
    private static final long serialVersionUID = 1662255962083814778L;
    /**
     * Error status.
     */
    Boolean error = false;
    /**
     * Error message.Must be initialized if errro is set to TRUE.
     */
    String errMsg = "";
    /**
     * Response to return from the server if there is no error.
     */
    String output = "";
}
