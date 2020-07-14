package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.services.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    final private Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private StorageManager storageManager;

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    /**
     * Switcher displays starter page for users without boards or user's list of boards.
     * @param viewModel Template model.
     * @return Template name.
     */
    @GetMapping("{lang}/app/{userId}/")
    public String displayHome(Model viewModel) {
        List<BoardModel> boards = storageManager.getBoards();
        if (boards.isEmpty()) {
            LOG.debug("No boards found. Displaying starter page.");
            return displayStarter(viewModel);
        }
        LOG.debug("Boards are found. Displaying boards' list.");
        return "test";
    }

    public String displayStarter(Model viewModel) {
        // Add custom fonts
        List<String> stylesheetsExt = new ArrayList<>();
        stylesheetsExt.add("https://fonts.googleapis.com/css2?family=Nothing+You+Could+Do&display=swap");
        viewModel.addAttribute("stylesheetsExt", stylesheetsExt);
        viewModel.addAttribute("title", "Title");
        return "app/starter";
    }
}
