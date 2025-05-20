package com.bookstore.server.core.util;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void testFormatDate_withValidDate_returnsFormattedString() {
        LocalDate date = LocalDate.of(1983, 5, 24);
        String formatted = DateUtil.formatDate(date);
        assertEquals("24 mai 1983", formatted);
    }

    @Test
    void testFormatDate_withNull_throwsException() {
        assertThrows(DateTimeException.class, () -> DateUtil.formatDate(null));
    }

}