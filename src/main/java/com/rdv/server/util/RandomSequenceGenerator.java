package com.rdv.server.util;

import org.springframework.stereotype.Component;

@Component
public class RandomSequenceGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SEQUENCE_LENGTH = 10;

    public static String generateAlphaNumericSequence() {
        StringBuilder builder = new StringBuilder();
        int count = SEQUENCE_LENGTH;
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
