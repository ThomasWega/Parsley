package me.wega.parsley;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompactNumberFormatterTest {
    @Test
    public void formatsSmallAndCompactNumbers() {
        assertEquals("0", CompactNumberFormatter.format("0"));
        assertEquals("123", CompactNumberFormatter.format("123"));
        assertEquals("1.0K", CompactNumberFormatter.format("1000"));
        assertEquals("1.234K", CompactNumberFormatter.format("1234"));
        assertEquals("12.345K", CompactNumberFormatter.format("12345"));
        assertEquals("1.235M", CompactNumberFormatter.format("1234567"));
        assertEquals("1.0B", CompactNumberFormatter.format("1000000000"));
        assertEquals("1.0T", CompactNumberFormatter.format("1000000000000"));
        assertEquals("1000.0T", CompactNumberFormatter.format("1000000000000000"));
    }

    @Test
    public void supportsNegativeNumbersAndRounding() {
        assertEquals("-1.235M", CompactNumberFormatter.format("-1234567"));
        assertEquals("1.234K", CompactNumberFormatter.format("1234.5"));
        assertEquals("1.0K", CompactNumberFormatter.format("999.95"));
        assertEquals("1.0M", CompactNumberFormatter.format("999999"));
        assertEquals("1.0K", CompactNumberFormatter.format("1,000"));
        assertEquals("2", CompactNumberFormatter.format("1.5"));
    }

    @Test
    public void preservesUnresolvedAndInvalidValues() {
        assertEquals("N/A", CompactNumberFormatter.format("N/A"));
        assertEquals("{pr_money}", CompactNumberFormatter.format("{pr_money}"));
        assertEquals("", CompactNumberFormatter.format(""));
    }
}
