package me.wega.parsley;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ParsleyExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "parsley";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Wega";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(final OfflinePlayer player, final @NotNull String params) {
        if (params.isEmpty()) return null;

        final int i = params.indexOf('_');
        if (i == -1) return null;

        final String type = params.substring(0, i).toLowerCase();

        // Resolve bracket placeholders FIRST, then split
        final String resolved = PlaceholderAPI.setBracketPlaceholders(player, params.substring(i + 1));
        final List<String> args = this.split(resolved);

        if (type.equals("ifempty") && args.size() == 3)
            return this.isEmpty(args.get(0)) ? args.get(1) : args.get(2);

        if (type.equals("isequal") && args.size() == 4) {
            final String value = args.get(0);
            final String compare = args.get(1);
            return value != null && value.equalsIgnoreCase(compare) ? args.get(2) : args.get(3);
        }

        if (type.equals("greater") && args.size() == 4) {
            final double value = this.toDouble(args.get(0));
            final double threshold = this.toDouble(args.get(1));
            return value > threshold ? args.get(2) : args.get(3);
        }

        if (type.equals("less") && args.size() == 4) {
            final double value = this.toDouble(args.get(0));
            final double threshold = this.toDouble(args.get(1));
            return value < threshold ? args.get(2) : args.get(3);
        }

        if (type.equals("contains") && args.size() == 4) {
            final String value = args.get(0);
            final String search = args.get(1);
            return value != null && value.toLowerCase().contains(search.toLowerCase()) ? args.get(2) : args.get(3);
        }

        if (type.equals("startswith") && args.size() == 4) {
            final String value = args.get(0);
            final String prefix = args.get(1);
            return value != null && value.toLowerCase().startsWith(prefix.toLowerCase()) ? args.get(2) : args.get(3);
        }

        if (type.equals("endswith") && args.size() == 4) {
            final String value = args.get(0);
            final String suffix = args.get(1);
            return value != null && value.toLowerCase().endsWith(suffix.toLowerCase()) ? args.get(2) : args.get(3);
        }

        if (type.equals("between") && args.size() == 5) {
            final double value = this.toDouble(args.get(0));
            final double min = this.toDouble(args.get(1));
            final double max = this.toDouble(args.get(2));
            return value >= min && value <= max ? args.get(3) : args.get(4);
        }

        return null;
    }

    private List<String> split(final String input) {
        final List<String> result = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        int bracketDepth = 0;
        boolean escaped = false;

        for (int j = 0; j < input.length(); j++) {
            final char c = input.charAt(j);

            // Handle escape sequences
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }
            if (c == '\\') {
                escaped = true;
                continue;
            }

            // Track bracket depth for < > and { }
            if (c == '<' || c == '{') bracketDepth++;
            if (c == '>' || c == '}') bracketDepth--;

            // Only split on underscore when not inside brackets
            if (c == '_' && bracketDepth == 0) {
                result.add(current.toString());
                current.setLength(0);
            } else current.append(c);
        }
        result.add(current.toString());
        return result;
    }

    private boolean isEmpty(final String value) {
        if (value == null || value.trim().isEmpty()) return true;
        final String lower = value.trim().toLowerCase();
        return lower.equals("null") || lower.equals("none");
    }

    private double toDouble(final String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.replace(",", "").replace("$", "").replace("€", "").replace("£", "").trim());
        } catch (final NumberFormatException e) {
            return 0.0;
        }
    }
}