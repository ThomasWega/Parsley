package me.wega.parsley;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Compact US-style numbers (1.234K, 1.235M) without requiring Java 12+. */
final class CompactNumberFormatter {
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1_000);
    private static final String[] SUFFIXES = {"", "K", "M", "B", "T"};

    private CompactNumberFormatter() {
    }

    static String format(final String input) {
        if (input == null || input.trim().isEmpty()) return input;

        final BigDecimal number;
        try {
            number = new BigDecimal(input.trim().replace(",", ""));
        } catch (final NumberFormatException exception) {
            // Leave missing or non-numeric placeholders unchanged rather than displaying 0.
            return input;
        }

        BigDecimal divisor = BigDecimal.ONE;
        int magnitude = 0;
        while (magnitude < SUFFIXES.length - 1
                && number.abs().compareTo(divisor.multiply(THOUSAND)) >= 0) {
            divisor = divisor.multiply(THOUSAND);
            magnitude++;
        }

        final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        if (magnitude == 0 && number.abs().setScale(1, RoundingMode.HALF_EVEN).compareTo(THOUSAND) >= 0) {
            divisor = THOUSAND;
            magnitude = 1;
        }

        if (magnitude == 0) {
            final DecimalFormat normal = new DecimalFormat("#,##0", symbols);
            normal.setRoundingMode(RoundingMode.HALF_EVEN);
            return normal.format(number);
        }

        BigDecimal scaled = number.divide(divisor, 3, RoundingMode.HALF_EVEN);
        if (magnitude < SUFFIXES.length - 1 && scaled.abs().setScale(1, RoundingMode.HALF_EVEN).compareTo(THOUSAND) >= 0) {
            divisor = divisor.multiply(THOUSAND);
            magnitude++;
            scaled = number.divide(divisor, 3, RoundingMode.HALF_EVEN);
        }

        final DecimalFormat compact = new DecimalFormat("0.0##", symbols);
        compact.setRoundingMode(RoundingMode.HALF_EVEN);
        return compact.format(scaled) + SUFFIXES[magnitude];
    }
}
