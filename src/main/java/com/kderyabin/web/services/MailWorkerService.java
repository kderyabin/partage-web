package com.kderyabin.web.services;

import com.kderyabin.web.model.MailActionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * MailWorkerService is a facade for the {@link MailService}.
 * Generates links used in email and sends asynchronously emails.
 */
@Service
public class MailWorkerService {
    final private Logger LOG = LoggerFactory.getLogger(MailWorkerService.class);
    private Environment env;
    private MailService mailService;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Generates link for the email confirmation.
     * @param actionModel   MailActionModel instance
     * @param lang          User language.
     * @return Link
     */
    public String getConfirmEmailLink(MailActionModel actionModel, String lang){
        return String.format("%s/%s/confirm-email/%s",
                env.getProperty("app.host", ""),
                lang,
                actionModel.getToken()
        );
    }
    /**
     * Sends asynchronously a confirmation email to the user.
     * Can be done with retry.
     *
     * @param actionModel ActionModel instance related to the current registration
     * @param lang        User language for email localization.
     */
    public void sendConfirmationEmail(MailActionModel actionModel, String lang) {
        CompletableFuture.runAsync(() -> {
            LOG.info("Start sending confirmation email");
            try {
                String confirmUrl = getConfirmEmailLink(actionModel, lang);
                mailService.setLocale(new Locale(lang));
                mailService.sendConfirmationMail(
                        actionModel.getUser().getLogin(),
                        actionModel.getUser().getName(),
                        confirmUrl
                );
            } catch (MessagingException e) {
                LOG.warn(e.toString());
            }
            LOG.info("End sending confirmation email");
        });
    }

    /**
     * Generates link for teh password reset.
     * @param actionModel   MailActionModel instance
     * @param lang          User language.
     * @return Link
     */
    public String getResetLink(MailActionModel actionModel, String lang){
        return String.format(
                "%s/%s/password-reset/%s",
                env.getProperty("app.host", ""),
                lang,
                actionModel.getToken()
        );
    }
    /**
     * Sends asynchronously a password reset link by email to the user.
     * Can be done with retry.
     *
     * @param actionModel   MailActionModel instance
     * @param lang          User language for email localization.
     */
    public void sendResetEmail(MailActionModel actionModel, String lang) {
        CompletableFuture.runAsync(() -> {
            LOG.info("Start sending reset email");
            try {
                String link = getResetLink(actionModel, lang);
                mailService.setLocale(new Locale(lang));
                mailService.sendResetPasswordMail( actionModel.getUser().getLogin(), actionModel.getUser().getName(), link);
            } catch (MessagingException e) {
                LOG.warn(e.toString());
            }
            LOG.info("End sending reset email");
        });
    }

}
