package com.kderyabin.web.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceTest {

	final private Logger LOG = LoggerFactory.getLogger(MailServiceTest.class);

	@Autowired
	MailService mailService;
	
	@Test
	void getMailContent() {
		Map<String, Object> templateModel = new HashMap<>();
		Locale locale = Locale.FRENCH;
		mailService.setLocale(locale);
		
		String recipientName = "Konstantin Deryabin";
		String link = "http://localhost:8080/fr/xxxxxxxxxxxxxxxxxxx";
		templateModel.put("recipientName", recipientName);
		templateModel.put("lang", locale.getLanguage());
		templateModel.put("link", link);

		String htmlBody = mailService.getMailContent("mail/confirm.html", locale, templateModel);

		// Test language is applied.
		assertTrue(htmlBody.contains("Bonjour"));
		assertTrue(htmlBody.contains(recipientName));
		assertTrue(htmlBody.contains(link));
	}


    @Test
    void sendConfirmationMail() throws MessagingException, IOException {
		String sender = "sender@somewhere.com";
		String recipientEmail = "kderyabin@somewhere.com";
		String recipientName = "Konstantin Deryabin";
		String link = "http://localhost:8080/fr/xxxxxxxxxxxxxxxxxxx";
		Locale locale = Locale.ENGLISH;
		mailService.setLocale(locale);
		mailService.setSendMail(false);
		mailService.setSenderAddress(sender);
		mailService.sendConfirmationMail(recipientEmail, recipientName, link);
		MimeMessage message = mailService.getMimeMessage();

		Address address = message.getRecipients(Message.RecipientType.TO)[0];
		LOG.debug(address.toString());
		assertEquals(recipientEmail, address.toString());

		Address from = message.getFrom()[0];
		assertEquals(sender, from.toString());
    }
}
