package com.kderyabin.web.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Handles application encryption
 */
@Service
public class SecurityService {

    final private Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    /**
     * Preconfigured salt string
     */
    @Value("${app.salt}")
    protected String salt;

    /**
     * Generates a SHA-256 hash of the user password.
     *
     * @return Hexadecimal value
     */
    public String getHashedPassword(String pwd) throws NoSuchAlgorithmException {
        MessageDigest digest;
        StringBuilder hexString = new StringBuilder();
        digest = MessageDigest.getInstance("SHA-512");
        digest.update(salt.getBytes(StandardCharsets.UTF_8));
        byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
        // Convert to hex value
        for (byte aByte : hash) {
            hexString.append(String.format("%02x", aByte));
        }

        return hexString.toString();
    }
}
