package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.services.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * BoardController manages display of everything related to the board.
 */
@Controller
@RequestMapping("{lang}/app/{userId}/board")
public class BoardController {
    final private Logger LOG = LoggerFactory.getLogger(BoardController.class);

    private StorageManager storageManager;

    private SettingsService settingsService;

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Displays a board form for a board creation.
     *
     * @return Template name
     */
    @GetMapping("/new")
    public String displayCreateForm(Model viewModel, HttpServletRequest request) {

        List<Currency> currencies = SettingsService.getAllCurrencies();
        viewModel.addAttribute("currencies", currencies);
        viewModel.addAttribute("userCurrency", settingsService.getCurrency());
        List<PersonModel> persons = storageManager.getPersons();
        viewModel.addAttribute("persons", persons);
        HttpSession session = request.getSession(false);

        String userId = (String) session.getAttribute("userId");
        String lang = (String) request.getAttribute("lang");
        viewModel.addAttribute("navbarBtnBackLink", String.format( "/%s/app/%s/", lang, userId ));

        return "app/board-edit";
    }

    /**
     * Handle board form submission.
     *
     * @return Template name in case of errors or redirection command.
     */
    @PostMapping("/new")
    public String handleCreateForm() {
        return "app/board-edit";
    }

    /**
     * Displays edit board form for a board update.
     *
     * @return Template name
     */
    @GetMapping("/{boardId}/edit")
    public String displayEditForm() {
        return "test";
    }

    /**
     * Handle board form submission.
     *
     * @return Template name in case of errors or redirection command.
     */
    @PostMapping("/{boardId}/edit")
    public String handleEditForm() {
        return "test";
    }

    /**
     * Displays board's details.
     *
     * @return Template name
     */
    @GetMapping("/{boardId}/details")
    public String displayDetails() {
        return "test";
    }

    /**
     * Displays board's balance.
     *
     * @return Template name
     */
    @GetMapping("/{boardId}/balance")
    public String displayBalance() {
        return "test";
    }
}
