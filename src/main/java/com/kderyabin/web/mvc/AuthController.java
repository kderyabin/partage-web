package com.kderyabin.web.mvc;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kderyabin.core.MailAction;
import com.kderyabin.core.model.MailActionModel;
import com.kderyabin.web.bean.ResetPassword;
import com.kderyabin.web.bean.ResetRequest;
import com.kderyabin.web.error.MailTokenNotFoundException;
import com.kderyabin.web.error.UserNotFoundException;
import com.kderyabin.web.services.MailService;
import com.kderyabin.web.services.SecurityService;
import com.kderyabin.web.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.services.AccountManager;
import com.kderyabin.web.bean.Signin;
import com.kderyabin.web.bean.Signup;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles account authentication and creation
 */
@Controller
public class AuthController {
    final private Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private AccountManager accountManager;
    private Environment env;
    private MailService mailService;
    private SecurityService securityService;

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Display sign in form.
     *
     * @return View name.
     */
    @GetMapping("{lang}/signin")
    public String displaySignin() {

        return "signin";
    }

    /**
     * Validate authentication form.
     *
     * @param model     Signin bean populated by spring with form data
     * @param viewModel Empty view model used in case of error to display error message
     * @param request   Request
     * @return View name or redirect command.
     */
    @PostMapping("{lang}/signin")
    public String authenticate(@ModelAttribute Signin model, Model viewModel, HttpServletRequest request) {

        FormValidator<Signin> validator = new SigninValidator();
        validator.validate(model);
        if (validator.isValid()) {
            LOG.debug(model.toString());
            UserModel user = accountManager.findUserByLoginPassword(
                    model.getLogin(),
                    securityService.getHashedPassword(model.getPwd().trim())
            );

            if (user != null) {
                LOG.debug("Found user: " + user.toString());
                if (!user.getIsConfirmed()) {
                    validator.addMessage("generic", "error.account_confirm_email");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    return ("redirect:app/" + user.getId() + "/");
                }
            } else {
                validator.addMessage("generic", "error.account_invalid_credentials");
            }
        }
        viewModel.addAttribute("login", model.getLogin());
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        return "signin";
    }

    /**
     * Displays sign up form
     *
     * @return View name.
     */
    @GetMapping("{lang}/signup")
    public String displaySignup() {
        return "signup";
    }

    /**
     * Validates sign up form data, creates user and sends validation email.
     *
     * @return View name in case of error or redirects to email confirmation message.
     */
    @PostMapping("{lang}/signup")
    public String register(@PathVariable String lang, @ModelAttribute Signup dto, Model viewModel) {
        // Validate form fields
        FormValidator<Signup> validator = new SignupValidator();
        validator.validate(dto);
        LOG.debug(dto.toString());
        if (validator.isValid()) {
            // Check if login is in use
            UserModel userModel = new UserModel(dto.getLogin(), null);
            boolean exists = accountManager.isUserExists(userModel);
            LOG.debug("User found status: " + exists);
            if (!exists) {
                // Complete the model and process the account creation.
                userModel.setName(dto.getName());
                userModel.setIsConfirmed(false);
                String hashPwd = securityService.getHashedPassword(dto.getPwd().trim());
                userModel.setPwd(hashPwd);
                MailActionModel actionModel = accountManager.create(userModel);
                // Send email
                sendConfirmationEmail(actionModel, lang, env.getProperty("app.host", ""));
                return String.format("redirect:/%s/confirm-email", lang);
            } else {
                validator.addMessage("login", "error.email_in_use");
            }

        }
        viewModel.addAttribute("name", dto.getName());
        viewModel.addAttribute("login", dto.getLogin());
        viewModel.addAttribute("pwd", dto.getPwd());
        viewModel.addAttribute("confirmPwd", dto.getConfirmPwd());
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }

        return "signup";
    }

    /**
     * Displays a page with invitation for the email validation after the account setup.
     *
     * @return ModelAndView
     */
    @GetMapping("{lang}/confirm-email")
    public ModelAndView displayConfirmEmail() {
        ModelAndView viewModel = new ModelAndView("confirm-email");
        List<String> messages = new ArrayList<>();
        messages.add("msg.confirm_email");

        viewModel.addObject("messages", messages);
        return viewModel;
    }

    /**
     * Landing page for the email validation.
     *
     * @param lang      Language code.
     * @param token     User validation token sent by email.
     * @param viewModel Spring framework model to be populated in case of error.
     * @param request   Current request.
     * @return View name in case of a error or a redirection command.
     */
    @GetMapping("{lang}/confirm-email/{token}")
    public String validateEmail(@PathVariable String lang, @PathVariable String token, Model viewModel, HttpServletRequest request) {

        getValidMailAction(token, MailAction.CONFIRM);
        try {
            // Success
            UserModel user = accountManager.activateAccount(token);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return String.format("redirect:/%s/app/%s/", lang, user.getId());
        } catch (MailTokenNotFoundException e) {
            LOG.warn(e.getMessage());
        }
        // error case
        List<String> messages = new ArrayList<>();
        messages.add("error.token_validation_not_found");
        viewModel.addAttribute("messages", messages);
        return "confirm-email";
    }

    /**
     * Displays page for email sending in case of a password reset
     *
     * @return View name
     */
    @GetMapping("{lang}/password-reset")
    public String displayResetRequest() {
        return "password-request";
    }

    /**
     * Sends an email with a password reset link
     *
     * @param bean      Form data
     * @param viewModel Model instance
     * @return View name
     */
    @PostMapping("{lang}/password-reset")
    public String resetSendEmail(@PathVariable String lang, @ModelAttribute ResetRequest bean, Model viewModel) {
        FormValidator<ResetRequest> validator = new ResetRequestValidator();
        validator.validate(bean);
        if (validator.isValid()) {
            try {
                MailActionModel action = accountManager.registerResetRequest(bean.getLogin());
                String link = String.format(
                        "%s/%s/password-reset/%s",
                        env.getProperty("app.host", ""),
                        lang,
                        securityService.getEncryptedToken(action.getToken())
                );
                LOG.debug("Password reset generated link: " + link);
                sendResetEmail(action.getUser().getLogin(), action.getUser().getName(), link, lang);
                viewModel.addAttribute("email_sent", true);
            } catch (UserNotFoundException e) {
                validator.addMessage("login", "error.account_not_found");
            }
        }
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        return "password-request";
    }

    /**
     * Fetches mail action in database and checks if it matches the use case.
     *
     * @return MailActionModel
     * @throws ResponseStatusException 404 error
     */
    private MailActionModel getValidMailAction(String token, MailAction useCase) {
        MailActionModel action = accountManager.findMailActionByToken(token);
        if (action == null || action.getAction() != useCase) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return action;
    }

    /**
     * Displays page for email sending in case of a password reset
     *
     * @return View name
     * @throws ResponseStatusException 404 error if token is not valid
     */
    @GetMapping("{lang}/password-reset/{token}")
    public String displayPasswordReset(@PathVariable String token, Model viewModel) {
        getValidMailAction(token, MailAction.RESET);
        return "password-reset";
    }

    /**
     * Handles password reset form
     *
     * @return View name
     * @throws ResponseStatusException 404 error if token is not valid
     */
    @PostMapping("{lang}/password-reset/{token}")
    public String handleResetPassword(@PathVariable String token, @ModelAttribute ResetPassword bean, Model viewModel) {
        MailActionModel action = getValidMailAction(token, MailAction.RESET);
        FormValidator<ResetPassword> validator = new ResetPasswordValidation();
        validator.validate(bean);

        if (validator.isValid()) {
            UserModel user = action.getUser();
            user.setPwd(securityService.getHashedPassword(bean.getPwd().trim()));
            accountManager.save(user);
            accountManager.deleteMailAction(action);
            viewModel.addAttribute("success", true);
        }
        if (!validator.isValid()) {
            viewModel.addAttribute("errors", validator.getMessages());
        }
        return "password-reset";
    }

    /**
     * Sends asynchronously a confirmation email to the user.
     * Can be done with retry.
     *
     * @param actionModel ActionModel instance related to the current registration
     * @param lang        User language for email localization.
     * @param host        Application host.
     */
    private void sendConfirmationEmail(MailActionModel actionModel, String lang, String host) {
        CompletableFuture.runAsync(() -> {
            LOG.info("Start sending confirmation email");
            try {
                mailService.setLocale(new Locale(lang));
                mailService.sendConfirmationMail(
                        actionModel.getUser().getLogin(),
                        actionModel.getUser().getName(),
                        host,
                        actionModel.getToken()
                );
            } catch (MessagingException e) {
                LOG.warn(e.toString());
            }
            LOG.info("End sending confirmation email");
        });
    }

    /**
     * Sends asynchronously a password reset link by email to the user.
     * Can be done with retry.
     *
     * @param email User email
     * @param name  User name
     * @param link  Password reset link
     * @param lang  User language for email localization.
     */
    private void sendResetEmail(String email, String name, String link, String lang) {
        CompletableFuture.runAsync(() -> {
            LOG.info("Start sending reset email");
            try {
                mailService.setLocale(new Locale(lang));
                mailService.sendResetPasswordMail(email, name, link);
            } catch (MessagingException e) {
                LOG.warn(e.toString());
            }
            LOG.info("End sending reset email");
        });
    }
}
