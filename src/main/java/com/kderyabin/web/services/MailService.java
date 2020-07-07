package com.kderyabin.web.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Mail service handles email sending for different use cases. It uses Thymeleaf
 * template engine for email parsing and localization.
 * 
 * @author Konstantin Deryabin
 */
@Service
public class MailService {
	final private Logger LOG = LoggerFactory.getLogger(MailService.class);
	
	/**
	 * Application Sender email address.
	 */
	private String senderAddress;
	/**
	 * Locale used for email localization.
	 */
	private Locale locale;
	/**
	 * JavaMailSender instance.
	 */
	private JavaMailSender mailSender;
	/**
	 * Application resource bundle with messages.
	 */
	private MessageSource messageSource;
	/**
	 * Flag saying if generated email must be sent.
	 * Initialized in application properties.
	 * Default true. Can be set to false in dev env.
	 */
	@Value("${app.mail.send}")
	private Boolean sendMail;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Autowired
	public void setResourceBundle(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Boolean isSendMail() {
		return sendMail;
	}

	/**
	 * Application From address.
	 * @param senderAddress Email address.
	 */
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	/**
	 * Sends email to newly registered user for email address confirmation.
	 * 
	 * @param recipientEmail Receiver email.
	 * @param recipientName  Receiver name.
	 * @param host           Application host
	 * @param token          User token for account confirmation.
	 * @throws MessagingException
	 */
	public void sendConfirmationMail(String recipientEmail, String recipientName, String host, String token)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	
		Map<String, Object> templateModel = new HashMap<>();

		templateModel.put("recipientName", recipientName);
		templateModel.put("host", host);
		templateModel.put("lang", locale.getLanguage());
		templateModel.put("token", token);

		String htmlBody = getMailContent("mail/confirm.html", locale, templateModel);

		LOG.debug("Generated email content for " + recipientEmail);
		LOG.debug( htmlBody );

		helper.setFrom(senderAddress);
		helper.setTo(recipientEmail);
		helper.setText(htmlBody, true);
		helper.setSubject(messageSource.getMessage("email.confirm.subject", null, locale));

		if(!sendMail) {
			LOG.warn("Mail sending is disabled by application configuration");
			return;
		}

		mailSender.send(message);
	}

	/**
	 * Generates a mail content.
	 * 
	 * @param template Mail template name.
	 * @param locale   2 digit language code
	 * @param data     Data to be passed to template.
	 * @return Mail content in html format.
	 */
	public String getMailContent(String template, Locale locale, Map<String, Object> data) {
		Context context = new Context();
		context.setLocale(locale);
		context.setVariables(data);
		templateEngine.setTemplateEngineMessageSource(messageSource);
		
		String htmlBody = templateEngine.process(template, context);

		return htmlBody;
	}
}
