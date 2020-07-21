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

    public Notification() {
    }

    public Notification(String display) {
        this.display = display;
    }

    /**
     * Add message to dictionary.
     * @param key   Message key
     * @param msg   Message
     */
    public void addMessage(String key, String msg ){
        i18n.put(key, msg);
    }
}
