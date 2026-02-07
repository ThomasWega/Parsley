# 🌿 Parsley

A lightweight conditional placeholder expansion for [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

## Why Parsley?

I got tired of needing multiple expansions or writing super long placeholder logic just to show a value conditionally. Parsley keeps it simple.

**Bonus:** Bracket-aware splitting — underscores inside `<tags>` and `{placeholders}` are preserved automatically. No manual escaping needed.

## Installation

1. Download the latest release
2. Place in `/plugins/PlaceholderAPI/expansions/`
3. Run `/papi reload`

## Usage

- Use `{placeholder}` for internal placeholders
- Separate arguments with `_`
- Escape literal underscores with `\_`

## Placeholders

| Placeholder | Description |
|-------------|-------------|
| `%parsley_ifempty_<value>_<if-empty>_<if-not-empty>%` | Check if value is empty/null/none |
| `%parsley_isequal_<value>_<compare>_<if-equal>_<if-not-equal>%` | Case-insensitive equality |
| `%parsley_greater_<value>_<threshold>_<if-true>_<if-false>%` | Numeric greater than |
| `%parsley_less_<value>_<threshold>_<if-true>_<if-false>%` | Numeric less than |
| `%parsley_between_<value>_<min>_<max>_<if-true>_<if-false>%` | Numeric range (inclusive) |
| `%parsley_contains_<value>_<search>_<if-true>_<if-false>%` | Case-insensitive contains |
| `%parsley_startswith_<value>_<prefix>_<if-true>_<if-false>%` | Case-insensitive starts with |
| `%parsley_endswith_<value>_<suffix>_<if-true>_<if-false>%` | Case-insensitive ends with |

## Examples

| Use Case | Placeholder | Result |
|----------|-------------|--------|
| Conditional prefix | `%parsley_ifempty_{luckperms_prefix}__[{luckperms_prefix}]%` | `[Admin]` or empty |
| Level-based title | `%parsley_greater_{player_level}_100_Elite_Novice%` | `Elite` or `Novice` |
| Health warning | `%parsley_less_{player_health}_5_<red>Low HP!_<green>OK%` | `Low HP!` or `OK` |

## License

MIT