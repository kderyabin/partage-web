package com.kderyabin.web.services;

import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void getUserLocaleFromAcceptLanguageHeader() {
        String header="en,en-US;q=0.8,fr-FR;q=0.5,fr;q=0.3";
        Locale locale =Utils.getLocaleFromAcceptLanguageHeader(header);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    @Test
    void getDefaultLocaleFromAcceptLanguageHeader() {
        String header="en;q=0.3";
        Locale locale =Utils.getLocaleFromAcceptLanguageHeader(header);
        assertEquals("fr", locale.getLanguage());
        assertEquals("FR", locale.getCountry());
    }
}