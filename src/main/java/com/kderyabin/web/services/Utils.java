package com.kderyabin.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Utils {

    final static private Logger LOG = LoggerFactory.getLogger(Utils.class);
    /**
     * Parses "Accept-Language" header and returns a Locale.
     * @param header "Accept-Language" header value
     * @return Locale
     */
    public static Locale getLocaleFromAcceptLanguageHeader(String header){
        Locale defaultLocale = Locale.FRANCE;
        String[] chunks = header.split(",");
        if (chunks.length == 0) {
            return defaultLocale;
        }
        LOG.debug("Chunks: " + chunks.length);
        Optional<String> langAndCountry = Arrays.stream(chunks)
                .map(item -> {
                    item = item.trim();
                    item = item.replaceAll(";q=.*", "");
                    return item;
                })
                .filter(item -> item.contains("-"))
                .findFirst();

        if( !langAndCountry.isPresent()) {
            // return default locale
            return defaultLocale;
        }
        LOG.debug("Found candidate: " + langAndCountry.get());
        String[] pieces = langAndCountry.get().split("-");
        return new Locale(pieces[0], pieces[1]);
    }
}
