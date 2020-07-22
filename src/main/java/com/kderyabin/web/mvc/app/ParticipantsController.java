package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.JsonResponseBody;
import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.utils.StaticResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles display of participants' list, edition and deletion of a participant.
 */
@Controller
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
     * Displays a list of all participants.
     * @return Template name.
     */
    @GetMapping("{lang}/app/{userId}/participants")
    public String displayList(Model viewModel, @PathVariable String lang, @PathVariable String userId){
        List<PersonModel> participants = storageManager.getPersons();
        viewModel.addAttribute("participants", participants);
        viewModel.addAttribute("title", messageSource.getMessage("participants", null, settingsService.getLanguage()));
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
     * Displays a participant form for edition.
     * @return Template name.
     */
    @GetMapping("{lang}/app/{userId}/participants/edit/{participantId}")
    public String displayForm(){
        return "app/participant-edit";
    }

    /**
     * Handles from submission.
     * @return  Redirect command on success or template name in case of error.
     */
    @PostMapping("{lang}/app/{userId}/participants/edit/{participantId}")
    public String handleForm(){
        return "app/participant-edit";
    }

    /**
     * Removed a participant from database.
     * @param person    Populated by Spring instance of PersonModel
     * @return          Message and status of the operation.
     */
    @PostMapping(
            value = "{lang}/app/{userId}/participants/remove",
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
