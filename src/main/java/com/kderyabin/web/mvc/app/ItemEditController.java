package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Item;
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
import java.util.Locale;
import java.util.Optional;

/**
 * Handles board item creation and update.
 */
@Controller
public class ItemEditController {

    final private Logger LOG = LoggerFactory.getLogger(ItemEditController.class);

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
     * Initializes basic data for the display.
     * This dtaa must be completed in each case.
     *
     * @param viewModel Model
     * @param lang      Current language
     * @param userId    User ID
     * @param boardId   Board ID
     * @return          Model with data.
     */
    protected Model initFormModel(Model viewModel, String lang, String userId, Long boardId) {
        // Default item.
        viewModel.addAttribute("model", new Item());
        // Init "Go back" link
        viewModel.addAttribute("navbarBtnBackLink", String.format("/%s/app/%s/board/%s/details", lang, userId, boardId));
        // Enable save button
        viewModel.addAttribute("navbarBtnSave", true);

        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_EDIT_ITEM);
        viewModel.addAttribute("scripts", scripts);

        return viewModel;
    }

    /**
     * Initializes data to render in edit board template
     *
     * @param viewModel Model instance
     * @param request   Request
     * @return          Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/item")
    public String displayForm(
            Model viewModel,
            HttpServletRequest request,
            @PathVariable String lang,
            @PathVariable String userId,
            @PathVariable Long boardId,
            @RequestParam(name = "iid", required = false) Long itemId
    ) {
        Item model;
        String title;
        if (itemId != null) {
            BoardItemModel itemModel = storageManager.findItemById(itemId);
            if (itemModel == null || !itemModel.getBoard().getId().equals(boardId)) {
                LOG.warn("Item not found in database: " + itemId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            model = Item.getItem(itemModel);
            title = model.getTitle();
        } else {
            model = new Item();
            title = messageSource.getMessage("new_entry", null, settingsService.getLanguage());
        }

        // Get list of all persons registered in all boards for selection list
        List<PersonModel> participants = storageManager.getParticipantsByBoardId(boardId);
        viewModel = initFormModel(viewModel, lang, userId, boardId);
        viewModel.addAttribute("participants", participants);
        viewModel.addAttribute("model", model);
        viewModel.addAttribute("title", title);

        if(itemId != null) {
            // Enable delete button
            viewModel.addAttribute("navbarBtnDelete", true);
        }
        HttpSession session = request.getSession(false);

        // Notification from a board creation.
        Notification notification = (Notification) session.getAttribute("notification");
        if(notification != null) {
            viewModel.addAttribute("notification", notification);
            session.removeAttribute("notification");
        }

        return "app/item-edit.jsp";
    }

    /**
     * Handles form submission.
     * @param bean      Form data populated into an Item bean.
     * @param viewModel Model instance
     * @param request   Current request
     * @param lang      Language code
     * @param userId    User ID
     * @param boardId   Board ID to which item is attached
     * @param itemId    item ID in case of update.
     * @return          Template name in case of error or redirect command on succes.
     */
    @PostMapping("{lang}/app/{userId}/board/{boardId}/item")
    public String handleSubmit(
            @ModelAttribute Item bean,
            Model viewModel,
            HttpServletRequest request,
            @PathVariable String lang,
            @PathVariable String userId,
            @PathVariable Long boardId,
            @RequestParam(name = "iid", required = false) Long itemId
    ) {
        LOG.info("Start item handleSubmit");
        LOG.debug("Received data: " + bean.toString());
        Notification notification = null;
        FormValidator<Item> validator = new FormValidatorImpl<>();
        validator.validate(bean);
        // Check selected person makes part of boarder participants
        List<PersonModel> participants = storageManager.getParticipantsByBoardId(boardId);

        if (validator.isValid()) {
            Optional<PersonModel> found = participants
                    .stream()
                    .filter(p -> p.getId().equals(bean.getParticipant()))
                    .findFirst();
            if (found.isEmpty()) {
                validator.addMessage("participant", "msg.person_is_required");

            }
            try{
                if( validator.isValid() ) {
                    // Load related BoardModel
                    BoardModel boardModel = storageManager.findBoardById(boardId);
                    BoardItemModel model = bean.getModel();
                    model.setBoard(boardModel);
                    model.setPerson(found.get());
                    if(itemId != null){
                        model.setId(itemId);
                    }
                    LOG.debug("Generated Model: " + model.toString());
                    storageManager.save(model);
                }
                notification = new Notification(messageSource.getMessage("msg.item_saved_success",null, settingsService.getLanguage()));
                HttpSession session = request.getSession(false);
                session.setAttribute("notification", notification);

                return "redirect:" + String.format("/%s/app/%s/board/%s/details", lang, userId, boardId);

            } catch (Exception e)  {
               LOG.warn(e.getMessage());
               notification = new Notification(messageSource.getMessage("msg.generic_error",null, settingsService.getLanguage()));
            }
        }
        viewModel = initFormModel(viewModel, lang, userId, boardId);
        viewModel.addAttribute("participants", participants);
        viewModel.addAttribute("model", bean);
        if(!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        if( notification != null) {
            viewModel.addAttribute("notification", notification);
        }
        String title = itemId != null
                ? bean.getTitle()
                : messageSource.getMessage("new_entry", null, settingsService.getLanguage());
        viewModel.addAttribute("title", title);

        return "app/item-edit.jsp";
    }

    /**
     *
     * @param bean
     * @param lang
     * @param userId
     * @param boardId
     * @param request
     * @return
     */
    @PostMapping("{lang}/app/{userId}/board/{boardId}/item/remove")
    public String removeItem(
            @ModelAttribute Item bean,
            @PathVariable String lang,
            @PathVariable String userId,
            @PathVariable String boardId,
            HttpServletRequest request
    ) {
        LOG.debug("Removing item with id: " + bean.getId());
        Locale locale = new Locale(lang);
        Notification notification;
        HttpSession session = request.getSession(false);
        // Default redirect url
        String url = String.format("/%s/app/%s/board/%s/details", lang, userId, boardId);
        if (bean.getId() == null) {
            // Something went wrong - the item id is lost
            notification = new Notification(messageSource.getMessage("msg.generic_error",null, locale));
        } else {
            try {
                storageManager.removeItemById(bean.getId());
                notification = new Notification(messageSource.getMessage("msg.item_delete_success",null, locale));
                LOG.debug("Item with removed with success, id: " + bean.getId());
            } catch (Exception e) {
                LOG.warn(e.getMessage());
                notification = new Notification(messageSource.getMessage("msg.generic_error",null, locale));
                url = String.format("/%s/app/%s/board/%s/item?iid=%s", lang, userId, boardId, bean.getId());
            }
        }

        session.setAttribute("notification", notification);
        return "redirect:"+url;
    }
}
