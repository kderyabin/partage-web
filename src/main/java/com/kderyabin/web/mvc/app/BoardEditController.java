package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Board;
import com.kderyabin.web.bean.JsonResponseBody;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.utils.StaticResources;
import com.kderyabin.web.validator.FormValidator;
import com.kderyabin.web.validator.FormValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class BoardEditController {

    final private Logger LOG = LoggerFactory.getLogger(BoardEditController.class);

    private static final String EDIT_MODE = "mode";
    private static final String MODE_CREATE = "create";
    private static final String MODE_EDIT = "edit";

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
     * Initializes data to render in edit board template
     *
     * @param viewModel
     * @param request
     * @return
     */
    protected Model initEditFormModel(Model viewModel, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        BoardModel model = new BoardModel();
        // Init board currency
        List<Currency> currencies = SettingsService.getAllCurrencies();
        viewModel.addAttribute("currencies", currencies);
        model.setCurrency(settingsService.getCurrency());
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
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_EDIT_BOARD);
        viewModel.addAttribute("scripts", scripts);

        viewModel.addAttribute("model", model);

        return viewModel;
    }

    /**
     * Displays a board form for a board creation.
     *
     * @return Template name
     */
    @GetMapping("{lang}/app/{userId}/board/new")
    public String displayCreateForm(Model viewModel, HttpServletRequest request) {
        viewModel = initEditFormModel(viewModel, request);
        HttpSession session = request.getSession(false);

        // Init session data
        session.setAttribute("persons", viewModel.getAttribute("persons"));
        session.setAttribute("participants", new ArrayList<PersonModel>());
        session.setAttribute(EDIT_MODE, MODE_CREATE);

        return "app/board-edit";
    }

    /**
     * Removes a board from database.
     *
     * @param board   BoardModel instance constructed from received param
     * @param request Current request
     * @return
     */
    @PostMapping(value = "{lang}/app/{userId}/board/remove-board", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonResponseBody> removeBoard(
            @RequestBody BoardModel board,
            HttpServletRequest request
    ) {
        JsonResponseBody responseBody = new JsonResponseBody();
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        if (board.getId() == null) {
            responseBody.setError(true);
            responseBody.setErrMsg(messageSource.getMessage("msg.generic_error", null, locale));
            return ResponseEntity.ok(responseBody);
        }
        try {
            storageManager.removeBoard(board);
            responseBody.setOutput(messageSource.getMessage("msg.board_deleted_success", null, locale));
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            responseBody.setErrMsg(messageSource.getMessage("msg.generic_error", null, locale));
        }

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Checks id participant can be added to board.
     * If yes: adds it to stored in session list of participants
     * If no: outputs the error message which can be handled be the frontend javascript.
     *
     * @param person  PersonModel instance
     * @param request Current request (required for language settings)
     * @return
     * @throws Exception
     */
    @PostMapping(
            value = {"{lang}/app/{userId}/board/add-participant", "{lang}/app/{userId}/board/{boardId}/add-participant"},
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<JsonResponseBody> addParticipantItem(
            @RequestBody PersonModel person,
            HttpServletRequest request) throws Exception {

        LOG.debug("Received participant:" + person.toString());


        JsonResponseBody responseBody = new JsonResponseBody();
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        HttpSession session = request.getSession(false);
        LOG.debug("Session exits: " + (session != null));


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
        // Check person is already registered
        if (person.getId() == null) {
            found = persons.stream()
                    .filter(p -> p.getName().toLowerCase().equals(person.getName().toLowerCase()))
                    .collect(Collectors.toList());
            if (found.size() > 0) {
                responseBody.setError(true);
                responseBody.setErrMsg(messageSource.getMessage("msg.person_already_registered", null, locale));
                return ResponseEntity.ok(responseBody);
            }
        }

        // Checks are OK : add person to participants list
        participants.add(person);
        responseBody.setOutput(person.getName());
        LOG.debug("Persons in sessioin: " + persons.size());
        LOG.debug("participants in sessioin: " + participants.size());


        return ResponseEntity.ok(responseBody);
    }

    @PostMapping(
            value = {
                    "{lang}/app/{userId}/board/remove-participant",
                    "{lang}/app/{userId}/board/{boardId}/remove-participant"
            },
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<JsonResponseBody> removeParticipantItem(
            @RequestBody PersonModel person,
            HttpServletRequest request) throws Exception {

        LOG.debug("Received participant:" + person.toString());

        JsonResponseBody responseBody = new JsonResponseBody();
        String lang = (String) request.getAttribute("lang");
        Locale locale = new Locale(lang);
        HttpSession session = request.getSession(false);
        LOG.debug("Session exits: " + (session != null));

        List<PersonModel> participants = (ArrayList<PersonModel>) session.getAttribute("participants");
        LOG.debug("participants in sessioin: " + participants.size());
        // Find the person to remove
        List<PersonModel> found = participants.stream()
                .filter(p -> p.getName().toLowerCase().equals(person.getName().toLowerCase()))
                .collect(Collectors.toList());
        if (found.size() > 0) {
            participants.remove(found.get(0));
        }

        LOG.debug("participants in session: " + participants.size());


        return ResponseEntity.ok(responseBody);
    }

    /**
     * Displays edit board form for a board update.
     *
     * @return Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/edit")
    public String displayEditForm(Model viewModel, @PathVariable Long boardId, HttpServletRequest request) {

        BoardModel model = storageManager.findBoardById(boardId);
        List<PersonModel> participants = storageManager.getParticipants(model);
        model.setParticipants(participants);
        viewModel = initEditFormModel(viewModel, request);
        viewModel.addAttribute("model", model);
        HttpSession session = request.getSession(false);
        // Init session data
        session.setAttribute("persons", viewModel.getAttribute("persons"));
        session.setAttribute("participants", participants);
        session.setAttribute(EDIT_MODE, MODE_EDIT);

        return "app/board-edit";
    }

    /**
     * Handles submitted form.
     * @param lang      Language code
     * @param userId    Current User ID
     * @param boardId   Board ID (the value is empty in creation mode)
     * @param bean      Submitted data
     * @param viewModel Model instance to display data in case of error
     * @param request   Current request
     * @return  Template name or redirect command
     */
    @PostMapping( value = {
            "{lang}/app/{userId}/board/new",
            "{lang}/app/{userId}/board/{boardId}/edit"
    })
    protected String handleSubmit(
            @PathVariable String lang,
            @PathVariable String userId,
            @PathVariable(required = false) Long boardId,
            @ModelAttribute Board bean,
            Model viewModel,
            HttpServletRequest request
            )
    {
        LOG.info("Start board handleSubmit");
        FormValidator<Board> validator = new FormValidatorImpl<>();
        validator.validate(bean);
        BoardModel model = new BoardModel();
        if (boardId != null) {
            model.setId(boardId);
        }
        model.setCurrency(bean.getCurrency());
        model.setName(bean.getName());
        model.setDescription(bean.getDescription());
        HttpSession session = request.getSession(false);
        List<PersonModel> participants = (ArrayList<PersonModel>) session.getAttribute("participants");
        model.setParticipants(participants);
        if (participants.size() == 0) {
            validator.addMessage("participants", "msg.provide_participants");
        }
        Notification notification = null;
        if (validator.isValid()) {
            LOG.debug("Data is valid");
            try {
                if (session.getAttribute(EDIT_MODE) == MODE_EDIT) {
                    List<PersonModel> participantsPrev = storageManager.getParticipants(model);
                    // For removed participants: removed their items
                    participantsPrev.removeAll(model.getParticipants());
                    LOG.debug("Remove items for participants: " + participantsPrev.size());
                    if(!participantsPrev.isEmpty()) {
                        List<Long> ids = participantsPrev.stream().map(PersonModel::getId).collect(Collectors.toList());
                        storageManager.removePersonItems(ids, model.getId());
                    }
                }
                model = storageManager.save(model, true);
                StringBuilder redirectLink = new StringBuilder("redirect:");
                if (session.getAttribute(EDIT_MODE) == MODE_CREATE) {
                    redirectLink.append(String.format("/%s/app/%s/board/%s/item", lang, userId, model.getId()));
                } else {
                    redirectLink.append(String.format("/%s/app/%s/board/%s/details", lang, userId, model.getId()));
                }
                //redirectLink.append(String.format("/%s/app/%s/board/%s/item", lang, userId, model.getId()));
                // Remove session data
                session.removeAttribute("persons");
                session.removeAttribute("participants");
                session.removeAttribute(EDIT_MODE);
                session.setAttribute("msgDisplay", messageSource.getMessage("msg.board_saved_success", null, settingsService.getLanguage()));

                return redirectLink.toString();

            } catch (Exception e) {
                LOG.info("Failed to save in DB: " + e.getMessage());
                //validator.addMessage("generic", "msg.generic_error");
                notification = new Notification(messageSource.getMessage("msg.generic_error", null, settingsService.getLanguage() ));
            }
        }
        viewModel = initEditFormModel(viewModel, request);
        viewModel.addAttribute("model", model);
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        if( notification != null) {
            viewModel.addAttribute("notification", notification);
        }

        return "app/board-edit";
    }

}
