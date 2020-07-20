package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.services.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    final private Logger LOG = LoggerFactory.getLogger(HomeController.class);

    /**
     * Storage manager to work with user database.
     */
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
    public String displayHome(Model viewModel,  HttpServletRequest request) {
        List<BoardModel> boards = storageManager.getBoards();
        // Enable common buttons in the navbar
        viewModel.addAttribute("navbarBtnParticipantsLink", "participants");
        viewModel.addAttribute("navbarBtnAddBoardLink", "board/new");
        if (boards.isEmpty()) {
            LOG.debug("No boards found. Displaying starter page.");
            return displayStarter(viewModel);
        }
        LOG.debug("Boards are found. Displaying boards' list.");

        viewModel.addAttribute("boards", boards);
        return displayBoards(viewModel, request);
    }

    public String displayStarter(Model viewModel) {
        // Add custom fonts
        List<String> stylesheetsExt = new ArrayList<>();
        stylesheetsExt.add("https://fonts.googleapis.com/css2?family=Nothing+You+Could+Do&display=swap");
        viewModel.addAttribute("stylesheetsExt", stylesheetsExt);
        viewModel.addAttribute("title", "Title");
        return "app/starter";
    }

    public String displayBoards(Model viewModel, HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session.getAttribute("msgDisplay") != null) {
            Notification notification = new Notification();
            notification.setDisplay( (String) session.getAttribute("msgDisplay") );
            viewModel.addAttribute("notification", notification);
            session.removeAttribute("msgDisplay");
        }
        viewModel.addAttribute("title", "Title");
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add("mdc.dialog.js");
        scripts.add("jquery-3.5.1.js");
        scripts.add("home.js");

        viewModel.addAttribute("scripts", scripts);
        return "app/home";
    }
}
