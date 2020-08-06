package com.kderyabin.web.mvc.app;

import com.kderyabin.web.model.MailActionModel;
import com.kderyabin.core.model.SettingModel;
import com.kderyabin.web.model.UserModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.bean.Settings;
import com.kderyabin.web.error.DupplicateLoginException;
import com.kderyabin.web.services.MailWorkerService;
import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.storage.AccountManager;
import com.kderyabin.web.storage.multitenancy.TenantContext;
import com.kderyabin.web.utils.StaticResources;
import com.kderyabin.web.validator.FormValidator;
import com.kderyabin.web.validator.FormValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * SettingsController handles user settings updates.
 */
@Controller
@RequestMapping(value = "{lang}/app/{userId}/settings")
public class SettingsController {
    final private Logger LOG = LoggerFactory.getLogger(SettingsController.class);

    private StorageManager storageManager;
    private SettingsService settingsService;
    private MessageSource messageSource;
    private AccountManager accountManager;
    private MailWorkerService mailWorkerService;

    @Autowired
    public void setMailWorkerService(MailWorkerService mailWorkerService) {
        this.mailWorkerService = mailWorkerService;
    }

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

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
     * Initializes settings form model.
     * @param viewModel
     * @param userId
     * @param lang
     * @return
     */
    public Model initFormModel(Model viewModel, String userId, String lang, HttpServletRequest request) {


        Locale locale = new Locale(lang);
        // Set default Locale for Java currencies and languages native translation
        Locale.setDefault(locale);
        List<Locale> languages = settingsService.getAvailableLanguages();
        List<Currency> currencies = SettingsService.getAllCurrencies();

        HttpSession session = request.getSession(false);
        Notification notification = (Notification) session.getAttribute("notification");
        if (notification != null) {
            viewModel.addAttribute("notification", notification);
            session.removeAttribute("notification");
        }
        // Enable navbar buttons
        viewModel.addAttribute("navbarBtnBackLink", String.format("/%s/app/%s/", lang, userId));
        // Enable save button
        viewModel.addAttribute("navbarBtnSave", true);

        viewModel.addAttribute("languages", languages);
        viewModel.addAttribute("currencies", currencies);
        viewModel.addAttribute("title", messageSource.getMessage("settings", null, locale));
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_DIALOG);
        scripts.add(StaticResources.JS_JQUERY);
        scripts.add(StaticResources.JS_SETTINGS);
        viewModel.addAttribute("scripts", scripts);

        return viewModel;
    }

    /**
     * Display settings form.
     * @param viewModel Model instance
     * @param userId    Current user
     * @param lang      Language code
     * @param request   Current request
     * @return  Template name
     */
    @GetMapping("/")
    public String displayForm(Model viewModel, @PathVariable String userId, @PathVariable String lang, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserModel user = (UserModel) session.getAttribute("user");
        settingsService.load();
        Settings bean = new Settings();
        bean.setCurrency(settingsService.getCurrency().getCurrencyCode());
        // Languages set in the settings may differ from lang from the path
        // It's up to user to choose the language during authentication
        bean.setLanguage(settingsService.getLanguage().getLanguage());
        bean.setLogin(user.getLogin());
        LOG.debug("Bean data: " + bean.toString());

        viewModel = initFormModel(viewModel, userId, lang, request);
        viewModel.addAttribute("model", bean);

        return "app/settings.jsp";
    }

    /**
     * Handles submitted form. Saving is done in 2 separate processes:
     * - User settings are saved in user database.
     * - User name and login are saved in main database.
     * @param bean          Populated by Spring form data.
     * @param viewModel     Model to populate for error case
     * @param userId        User ID
     * @param lang          Language code
     * @param request       Current request
     * @return              Template name or redirect command
     */
    @PostMapping("/")
    public String handleForm(
            @ModelAttribute Settings bean,
            Model viewModel,
            @PathVariable String userId,
            @PathVariable String lang,
            HttpServletRequest request
    ) {
        LOG.debug("Received Bean data: " + bean.toString());
        // Init current Locale which will be used for error cases
        Locale locale = new Locale(lang);
        FormValidator<Settings> validator = new FormValidatorImpl<>();
        validator.validate(bean);
        Notification notification = null;
        if (validator.isValid()) {
            try {
                Locale localeNew = new Locale(bean.getLanguage());
                updateSettings(bean);
                updateLogin(bean, bean.getLanguage(), request);
                notification = new Notification(messageSource.getMessage(
                        "msg.settings_saved_success",
                        null,
                        localeNew
                ));
                HttpSession session = request.getSession(false);
                session.setAttribute("notification", notification);
                // If language has been changed redirect to the right language version
                return "redirect:" + String.format("/%s/app/%s/settings/", bean.getLanguage(), userId);
            } catch (DupplicateLoginException e) {
                validator.addMessage("login", "error.email_in_use");
            } catch (Exception e) {
                LOG.warn(e.getMessage());
                notification = new Notification(messageSource.getMessage(
                        "msg.generic_error",
                        null,
                        locale
                ));
            }
        }
        LOG.debug("Bean is not valid");
        viewModel = initFormModel(viewModel, userId, lang, request);
        viewModel.addAttribute("model", bean);
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        if (notification != null) {
            viewModel.addAttribute("notification", notification);
        }

        return "app/settings.jsp";
    }

    /**
     * Registers password change request and redirects to password change URL.
     * @param lang      Language code.
     * @param request   Current request
     * @return          Redirect command
     */
    @GetMapping("/reset-password")
    public String resetPassword(@PathVariable String lang, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        UserModel currentUser = (UserModel) session.getAttribute("user");
        // Switch database
        LOG.debug("Switch to main database");
        String tenantId = TenantContext.getId();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        // Generate password change request
        LOG.debug("Generate reset request");
        MailActionModel actionModel = accountManager.registerResetRequest(currentUser.getLogin());
        String redirectUrl = mailWorkerService.getResetLink(actionModel, lang);
        LOG.debug("Redirect URL: " + redirectUrl);
        // Switch back
        TenantContext.setTenant(tenantId);
        return "redirect:" + redirectUrl;
    }

    /**
     * Saves user settings in user's database.
     *
     * @param bean Populated by Spring form data.
     */
    protected void updateSettings(Settings bean) {
        List<SettingModel> settings = storageManager.getSettings();
        List<SettingModel> update = new ArrayList<>();
        // Prepare language setting if it has been changed
        Optional<SettingModel> langSettingOpt = settings
                .stream()
                .filter(m -> m.getName().equals(SettingsService.LANG_NAME))
                .findFirst();
        SettingModel langSetting = null;
        if (langSettingOpt.isEmpty()) {
            langSetting = new SettingModel(SettingsService.LANG_NAME, bean.getLanguage());
            LOG.debug("Created language to save: " + langSetting.toString());
        } else if (!langSettingOpt.get().getValue().equals(bean.getLanguage())) {
            langSetting = langSettingOpt.get();
            langSetting.setValue(bean.getLanguage());
            LOG.debug("language to update: " + langSetting.toString());
        }

        if (langSetting != null) {
            update.add(langSetting);
        }
        // Prepare currency setting if it has been changed
        Optional<SettingModel> currencyOpt = settings
                .stream()
                .filter(m -> m.getName().equals(SettingsService.CURRENCY_NAME))
                .findFirst();
        SettingModel currencySetting = null;
        if (currencyOpt.isEmpty()) {
            currencySetting = new SettingModel(SettingsService.CURRENCY_NAME, bean.getCurrency());
            LOG.debug("New currency to save: " + currencySetting.toString());
        } else if (!currencyOpt.get().getValue().equals(bean.getCurrency())) {
            currencySetting = currencyOpt.get();
            currencySetting.setValue(bean.getCurrency());
            LOG.debug("currency to update: " + currencySetting.toString());
        }
        if (currencySetting != null) {
            update.add(currencySetting);
        }

        if (!update.isEmpty()) {
            LOG.debug("Settings will be updated");
            storageManager.save(update);
        }
    }

    /**
     * Updates user login in main database and sends email confirmation email.
     *
     * @param bean  Form data
     * @param lang  User language for the email content localization
     * @param request   Current request.
     */
    protected void updateLogin(Settings bean, String lang, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserModel currentUser = (UserModel) session.getAttribute("user");

        if (currentUser.getLogin().equals(bean.getLogin())) {
            LOG.info("Login is not changed: user update is not required");
            return;
        }
        String tenantId = TenantContext.getId();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        // Check if login is in use
        UserModel userModel = new UserModel();
        userModel.setLogin(bean.getLogin());
        boolean exists = accountManager.isUserExists(userModel);
        LOG.debug("Is login is already in use: " + exists);

        if (exists) {
            TenantContext.setTenant(tenantId);
            throw new DupplicateLoginException();
        }
        // Complete the model and process the account creation.
        currentUser.setLogin(bean.getLogin());
        MailActionModel actionModel = accountManager.create(currentUser);
        // Reset user in session
        session.setAttribute("user", actionModel.getUser());
        TenantContext.setTenant(tenantId);
        // Send email
        mailWorkerService.sendConfirmationEmail(actionModel, lang);
    }
}
