package com.bookstore.server.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomCodeGeneratorTest {

    @Test
    void testGenerateAlphaNumericCode() {
        String code = RandomCodeGenerator.generateAlphaNumericCode();
        assertNotNull(code, "The code can't be null");
        assertEquals(5, code.length(), "The code length must be 10");
        assertTrue(code.matches("[A-Z0-9]{5}"), "The code must be alphanumeric and in uppercase.");
    }

}