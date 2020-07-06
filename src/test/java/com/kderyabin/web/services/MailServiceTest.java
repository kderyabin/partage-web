package com.kderyabin.web.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailServiceTest {

	@Autowired
	MailService mailService;
	
	@Test
	void getMailContent() {
		Map<String, Object> templateModel = new HashMap<>();
		Locale locale = Locale.FRENCH;

		mailService.setLocale(locale);
		
		String recipientName = "Konstantin Deryabin";
		String host = "http://localhost:8080";
		String token = "xxxxxxxxxxxxxxxxxxx";
		templateModel.put("recipientName", recipientName);
		templateModel.put("host", host);
		templateModel.put("lang", locale.getLanguage());
		templateModel.put("token", token);

		String htmlBody = mailService.getMailContent("mail/confirm.html", locale, templateModel);

		// Test language is applied.		
		if(locale.getLanguage().equals((Locale.ENGLISH).getLanguage())) {
			assertTrue(htmlBody.contains("Hello"));
		} else if (locale.getLanguage().equals((Locale.FRENCH).getLanguage())) {
			assertTrue(htmlBody.contains("Bonjour"));
		} 
		
		assertTrue(htmlBody.contains(recipientName));
		assertTrue(htmlBody.contains(host));
		assertTrue(htmlBody.contains(token));
	}
	
	@Test
	void sendConfirmationMail() {
		
		Locale locale = Locale.ENGLISH;
		
		mailService.setLocale(locale);
		mailService.setSenderAddress("konst93@hotmail.com");
		String recipientEmail = "kderyabin@orange.fr";
		String recipientName = "Konstantin Deryabin";
		String host = "http://localhost:8080";
		String token = "xxxxxxxxxxxxxxxxxxx";
		
		try {
			mailService.sendConfirmationMail(recipientEmail, recipientName, host, token);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}
