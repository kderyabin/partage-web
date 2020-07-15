package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.JsonResponseBody;
import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.utils.SimpleHttpServletRequest;
import com.kderyabin.web.utils.SimpleHttpServletResponse;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * BoardController manages display of everything related to the board.
 */
@Controller
@RequestMapping("{lang}/app/{userId}/board")
public class BoardController {
    final private Logger LOG = LoggerFactory.getLogger(BoardController.class);

    private static final String MODE_CREATE = "board-create";
    private static final String MODE_EDIT = "board-edit";

    private StorageManager storageManager;

    private SettingsService settingsService;

    private MessageSource messageSource;

    private ViewResolver viewResolver;

    @Autowired
    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    @Autowired
    public void setResourceBundle(MessageSource messageSource) {
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
     * Displays a board form for a board creation.
     *
     * @return Template name
     */
    @GetMapping("/new")
    public String displayCreateForm(Model viewModel, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        // Init board currency
        List<Currency> currencies = SettingsService.getAllCurrencies();
        viewModel.addAttribute("currencies", currencies);
        viewModel.addAttribute("userCurrency", settingsService.getCurrency());
        // Get list of all persons registered in all boards for selection list
        List<PersonModel> persons = storageManager.getPersons();
        viewModel.addAttribute("persons", persons);

        // Init "Go back" link
        String userId = (String) session.getAttribute("userId");
        viewModel.addAttribute("navbarBtnBackLink", String.format("/%s/app/%s/", lang, userId));

        // Enable save button
        viewModel.addAttribute("navbarBtnSave", true);

        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add("mdc.dialog.js");
        scripts.add("jquery-3.5.1.js");
        scripts.add("edit-board.js");
        viewModel.addAttribute("scripts", scripts);

        // Init session data
        session.setAttribute("persons", persons);
        session.setAttribute("participants", new ArrayList<PersonModel>());
        session.setAttribute(MODE_CREATE, true);

        return "app/board-edit";
    }

    /**
     * Checks id participant cab ne added to board.
     * If yes: generates a line of participant in html format.
     * If no: outputs the error message.
     *
     * @param person  PersonModel instance
     * @param request Current request (required for language settings)
     * @return
     * @throws Exception
     */
    @PostMapping(value = "add-participant", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonResponseBody> addParticipantItem(
            @RequestBody PersonModel person,
            HttpServletRequest request) throws Exception {

        LOG.debug("Received participant:" + person.toString());


        JsonResponseBody responseBody = new JsonResponseBody();
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        HttpSession session = request.getSession(false);
        LOG.debug("Session exits: " + (session != null));

        if ((Boolean) session.getAttribute(MODE_CREATE)) {
            List<PersonModel> persons = (ArrayList<PersonModel>) session.getAttribute("persons");
            List<PersonModel> participants = (ArrayList<PersonModel>) session.getAttribute("participants");
            LOG.debug("Persons in sessioin: " + persons.size());
            LOG.debug("participants in sessioin: " + participants.size());
            // Check id the person is already registered for the board
            List<PersonModel> found = participants.stream()
                    .filter(p -> p.getName().toLowerCase().equals(person.getName().toLowerCase()))
                    .collect(Collectors.toList());
            if (found.size() > 0) {
                responseBody.setError(true);
                responseBody.setErrMsg(messageSource.getMessage("msg.participant_already_on_board", null, locale));
                return ResponseEntity.ok(responseBody);
            }
            // Check person is not registered yet
            found = persons.stream()
                    .filter(p -> p.getName().toLowerCase().equals(person.getName().toLowerCase()))
                    .collect(Collectors.toList());
            if (found.size() > 0) {
                responseBody.setError(true);
                responseBody.setErrMsg(messageSource.getMessage("msg.person_already_registered", null, locale));
                return ResponseEntity.ok(responseBody);
            }

            // Checks are OK : add person to participants list
            participants.add(person);
            responseBody.setOutput(person.getName());
            LOG.debug("Persons in sessioin: " + persons.size());
            LOG.debug("participants in sessioin: " + participants.size());
        }

        return ResponseEntity.ok(responseBody);
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
