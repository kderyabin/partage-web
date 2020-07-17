package com.kderyabin.web.mvc.app;

import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.services.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * BoardController manages display of everything related to the board.
 */
@Controller
public class BoardController {
    final private Logger LOG = LoggerFactory.getLogger(BoardController.class);

    private StorageManager storageManager;

    private SettingsService settingsService;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Displays board's details.
     *
     * @return Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/details")
    public String displayDetails() {
        return "test";
    }

    /**
     * Displays board's balance.
     *
     * @return Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/balance")
    public String displayBalance() {
        return "test";
    }
}
