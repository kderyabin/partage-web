package com.kderyabin.web.bean;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter @Setter
public class Notification {
    /**
     * Message to display
     */
    private String display = "";
    /**
     * Dictionary of translated messages.
     */
    private Map<String, String> i18n = new HashMap<>();
}
