package com.bookstore.server.core.util;

/**
 * @author davidgarcia
 */
public class RandomCodeGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;

    public static String generateAlphaNumericCode() {
        StringBuilder builder = new StringBuilder();
        int count = CODE_LENGTH;
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
