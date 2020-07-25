package com.kderyabin.web.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void isSendMail() {
		// mail sending is disabled for tests
		assertFalse(mailService.isSendMail());
    }
}
