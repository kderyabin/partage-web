package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.utils.StaticResources;
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
import java.util.Locale;

@Controller
public class HomeController {
    final private Logger LOG = LoggerFactory.getLogger(HomeController.class);

    /**
     * Storage manager to work with user database.
     */
    private StorageManager storageManager;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    /**
     * Switcher displays starter page for users without boards or user's list of boards.
     *
     * @param viewModel Template model.
     * @return Template name.
     */
    @GetMapping("{lang}/app/{userId}/")
    public String displayHome(@PathVariable String lang, Model viewModel, HttpServletRequest request) {
        Locale locale = new Locale(lang);
        List<BoardModel> boards = storageManager.getRecentBoards(30);
        // Enable common buttons in the navbar
        viewModel.addAttribute("navbarBtnParticipantsLink", "participants/");
        viewModel.addAttribute("navbarBtnAddBoardLink", "board/new");
        viewModel.addAttribute("navbarBtnSettings", true);
        viewModel.addAttribute("title", messageSource.getMessage(
                "title.your_boards",
                null,
                locale
        ));
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
        stylesheetsExt.add(StaticResources.CSS_HANDWRITE_FONT);
        viewModel.addAttribute("stylesheetsExt", stylesheetsExt);
        return "app/starter.jsp";
    }

    public String displayBoards(Model viewModel, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Notification notification = (Notification) session.getAttribute("notification");
        if (notification != null) {
            viewModel.addAttribute("notification", notification);
            session.removeAttribute("notification");
        }

        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_HOME);

        viewModel.addAttribute("scripts", scripts);
        return "app/home.jsp";
    }
}
