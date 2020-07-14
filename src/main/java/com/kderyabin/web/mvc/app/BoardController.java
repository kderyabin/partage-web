package com.kderyabin.web.mvc.app;

import com.kderyabin.core.services.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * BoardController manages display of everything related to the board.
 */
@Controller
@RequestMapping("{lang}/app/{userId}/board")
public class BoardController {
    final private Logger LOG = LoggerFactory.getLogger(BoardController.class);

    private StorageManager storageManager;

    /**
     * Displays a board form for a board creation.
     * @return Template name
     */
    @GetMapping("/new")
    public String displayCreateForm(){
        return "test";
    }

    /**
     * Handle board form submission.
     * @return Template name in case of errors or redirection command.
     */
    @PostMapping("/new")
    public String handleCreateForm(){
        return "test";
    }
    /**
     * Displays edit board form for a board update.
     * @return Template name
     */
    @GetMapping("/{boardId}/edit")
    public String displayEditForm(){
        return "test";
    }

    /**
     * Handle board form submission.
     * @return Template name in case of errors or redirection command.
     */
    @PostMapping("/{boardId}/edit")
    public String handleEditForm(){
        return "test";
    }

    /**
     * Displays board's details.
     * @return Template name
     */
    @GetMapping("/{boardId}/details")
    public String displayDetails(){
        return "test";
    }
    /**
     * Displays board's balance.
     * @return Template name
     */
    @GetMapping("/{boardId}/balance")
    public String displayBalance(){
        return "test";
    }
}
