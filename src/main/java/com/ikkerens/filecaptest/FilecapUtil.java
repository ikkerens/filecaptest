package com.ikkerens.filecaptest;

import java.security.SecureRandom;

public class FilecapUtil {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateFilecapID() {
        final StringBuilder sb = new StringBuilder(25 + 4);
        sb.append("iiii");
        for (int i = 0; i < 25; i++) {
            final int randomIndex = secureRandom.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(randomIndex));
        }
        return sb.toString();
    }
}
