package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.JsonResponseBody;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.bean.Person;
import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.utils.StaticResources;
import com.kderyabin.web.validator.FormValidator;
import com.kderyabin.web.validator.FormValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles display of participants' list, edition and deletion of a participant.
 */
@Controller
@RequestMapping( value = "{lang}/app/{userId}/participants")
public class ParticipantsController {
    final private Logger LOG = LoggerFactory.getLogger(ParticipantsController.class);

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
     * Generates a base link for the participant's area which end with a slash.
     * @param lang      Language code
     * @param userId    User ID
     * @return Link
     */
    protected String getSectionlink(String lang, String userId){
        return String.format("/%s/app/%s/participants/", lang, userId);
    }
    /**
     * Displays a list of all participants.
     * @return Template name.
     */
    @GetMapping("/")
    public String displayList(Model viewModel, @PathVariable String lang, @PathVariable String userId, HttpServletRequest request){
        List<PersonModel> participants = storageManager.getPersons();
        viewModel.addAttribute("participants", participants);
        viewModel.addAttribute("title", messageSource.getMessage("participants", null, settingsService.getLanguage()));
        HttpSession session = request.getSession(false);
        // Notification can be set in edit form
        Notification notification = (Notification) session.getAttribute("notification");
        if(notification !=  null) {
            viewModel.addAttribute("notification", notification);
            session.removeAttribute("notification");
        }
        // Enable navbar buttons
        viewModel.addAttribute("navbarBtnBackLink", String.format("/%s/app/%s/", lang, userId));
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_PARTICIPANTS);
        viewModel.addAttribute("scripts", scripts);

        return "app/participants";
    }

    /**
     * Initializes basic data for the display.
     * This d must be completed in each case.
     * Returned Model instance must be completed.
     *
     * @param viewModel Model
     * @param lang      Current language
     * @param userId    User ID
     * @return          Model with data.
     */
    protected Model initFormModel(Model viewModel, String lang, String userId) {
        // Default item.
        viewModel.addAttribute("model", new PersonModel());
        // Init "Go back" link²
        viewModel.addAttribute("navbarBtnBackLink", getSectionlink(lang, userId));
        // Enable save button
        viewModel.addAttribute("navbarBtnSave", true);
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_EDIT_PARTICIPANT);
        viewModel.addAttribute("scripts", scripts);

        return viewModel;
    }
    /**
     * Displays a participant form for edition.
     * @return Template name.
     */
    @GetMapping("/edit/{participantId}")
    public String displayForm(
            @PathVariable Long participantId,
            @PathVariable String lang,
            @PathVariable String userId,
            Model viewModel
    ){
        PersonModel person = storageManager.findPersonById(participantId);
        if(person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Person model = new Person();
        model.setName(person.getName());
        viewModel = initFormModel(viewModel, lang, userId);
        viewModel.addAttribute("model", model);
        viewModel.addAttribute("title", model.getName());

        return "app/participant-edit";
    }

    /**
     * Handles from submission.
     * @return  Redirect command on success or template name in case of error.
     */
    @PostMapping("/edit/{participantId}")
    public String handleForm(
            @PathVariable Long participantId,
            @ModelAttribute Person bean,
            @PathVariable String lang,
            @PathVariable String userId,
            Model viewModel,
            HttpServletRequest request
    ){
        LOG.info("Start save person form");
        LOG.debug("Data received : " + bean.toString());
        Notification notification = null;
        FormValidator<Person> validator = new FormValidatorImpl<>();
        validator.validate(bean);
        if(validator.isValid()) {
            try {
                PersonModel model = new PersonModel(bean.getName());
                model.setId(participantId);
                storageManager.save(model);

                notification = new Notification(messageSource.getMessage("msg.person_saved_success",null, settingsService.getLanguage()));
                HttpSession session = request.getSession(false);
                session.setAttribute("notification", notification);

                return "redirect:" +  getSectionlink(lang, userId);

            } catch (DataIntegrityViolationException e) {
                // Non unique name error
                LOG.warn(e.getMessage());
                notification = new Notification(messageSource.getMessage("msg.person_exists",null, settingsService.getLanguage()));
            } catch (Exception e) {
                LOG.warn(e.getMessage());
                notification = new Notification(messageSource.getMessage("msg.generic_error",null, settingsService.getLanguage()));
            }
        }
        viewModel = initFormModel(viewModel, lang, userId);
        viewModel.addAttribute("model", bean);
        viewModel.addAttribute("title", bean.getName());
        if(!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        if( notification != null) {
            viewModel.addAttribute("notification", notification);
        }

        return "app/participant-edit";
    }

    /**
     * Removes a participant from database.
     * @param person    Populated by Spring instance of PersonModel
     * @return          Message and status of the operation.
     */
    @PostMapping(
            value = "/remove",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<JsonResponseBody> removeParticipant(
            @RequestBody PersonModel person
    ){
        JsonResponseBody response= new JsonResponseBody();
        try{
            storageManager.removePerson(person);
            response.setOutput(messageSource.getMessage("msg.participant_deleted_success", null, settingsService.getLanguage()));
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            response.setError(true);
            response.setErrMsg(messageSource.getMessage("msg.generic_error", null, settingsService.getLanguage()));
        }

        return ResponseEntity.ok(response);
    }
}
