package com.kderyabin.web.mvc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kderyabin.core.model.MailActionModel;
import com.kderyabin.web.error.MailTokenNotFound;
import com.kderyabin.web.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.services.AccountManager;
import com.kderyabin.web.dto.Signin;
import com.kderyabin.web.dto.SignupDTO;
import com.kderyabin.web.validator.SigninValidator;
import com.kderyabin.web.validator.SignupValidator;

/**
 * Handles account authentication and creation
 */
@Controller
public class AuthController {
    final private Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private AccountManager accountManager;
    private Environment env;
    private MailService mailService;

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
     * @param model
     * @param viewModel
     * @param request
     * @return View name or redirect command.
     */
    @PostMapping("{lang}/signin")
    public String authenticate(@ModelAttribute Signin model, Model viewModel, HttpServletRequest request) {

        viewModel.addAttribute("login", model.getLogin());
        SigninValidator validator = new SigninValidator();
        validator.validate(model);
        if (validator.isValid()) {
            LOG.info(model.toString());
            UserModel foundUser = accountManager.findUserByLoginPassword(
                    model.getLogin(),
                    getHashedPassword(model.getPwd().trim())
            );

            if (foundUser == null) {
                viewModel.addAttribute("isFailedAuth", true);
                return "signin";
            } else {
                LOG.info("User found");
                LOG.info(foundUser.toString());
            }

        }
//		if (model.getLogin() == null || model.getPwd() == null) {
//			viewModel.addAttribute("isFailedAuth", true);
//			return "login";
//		}

//		HttpSession session = request.getSession();
//		session.setAttribute("currentUser", foundUser);

//		return "redirect:/sharings";
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
    public String register(@PathVariable String lang, @ModelAttribute SignupDTO dto, Model viewModel) {
        // Validate form fields
        SignupValidator validator = new SignupValidator();
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
                String hashPwd = getHashedPassword(dto.getPwd().trim());
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
     * @return View name 
     */
    @GetMapping("{lang}/confirm-email")
    public String displayConfirmEmail(Model viewModel) {
    	//msg.confirm_email
        List<String> messages = new ArrayList<>();
        messages.add("msg.confirm_email");
        viewModel.addAttribute("messages", messages);
        return "confirm-email";
    }


    @GetMapping("{lang}/sharing-app/{userId}/")
    public String displayUserSpace(@PathVariable String userId, Model viewModel){
        viewModel.addAttribute("userId", userId);
        return "sharing-app";
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
    @GetMapping("{lang}/confirm-email/{token}/")
    public String validateEmail(@PathVariable String lang, @PathVariable String token, Model viewModel, HttpServletRequest request) {
        try {
            // Success
            UserModel user = accountManager.activateAccount(token);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return String.format("redirect:/%s/sharing-app/%s/", lang, user.getId());
        } catch ( MailTokenNotFound e) {
            LOG.warn( e.getMessage());
        }
        // error case
        List<String> messages = new ArrayList<>();
        messages.add("error.token_validation_not_found");
        viewModel.addAttribute("messages", messages);
        return "confirm-email";
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
     * Generates a SHA-256 hash of the user password.
     *
     * @return Hexadecimal value
     */
    private String getHashedPassword(String pwd) {
        MessageDigest digest;
        String salt = env.getProperty("app.salt", "");
        StringBuffer hexString = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
            // Convert to hex value
            for (byte aByte : hash) {
                hexString.append(String.format("%02x", aByte));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

}
