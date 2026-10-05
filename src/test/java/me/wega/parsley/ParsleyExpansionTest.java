package me.wega.parsley;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ParsleyExpansionTest {

    private ParsleyExpansion expansion;
    private static MockedStatic<PlaceholderAPI> mockedPapi;

    @BeforeAll
    static void init() {
        if (Bukkit.getServer() == null) {
            Server server = Mockito.mock(CraftServer.class);
            Mockito.when(server.getLogger()).thenReturn(Logger.getLogger("Minecraft"));
            Mockito.when(server.getItemFactory()).thenReturn(Mockito.mock(org.bukkit.inventory.ItemFactory.class));
            Bukkit.setServer(server);
        }

        mockedPapi = Mockito.mockStatic(PlaceholderAPI.class);
        mockedPapi.when(() -> PlaceholderAPI.setBracketPlaceholders(Mockito.nullable(OfflinePlayer.class), Mockito.nullable(String.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));
    }

    @AfterAll
    static void tearDown() {
        if (mockedPapi != null) {
            mockedPapi.close();
        }
    }

    @BeforeEach
    void setUp() {
        expansion = new ParsleyExpansion();
    }

    @Test
    void testExpansionInfo() {
        assertEquals("parsley", expansion.getIdentifier());
        assertEquals("Wega", expansion.getAuthor());
        assertEquals("1.0.0", expansion.getVersion());
    }

    @Test
    void testIsEmptyPlaceholder() {
        assertEquals("true", expansion.onRequest(null, "isempty_"));
        assertEquals("true", expansion.onRequest(null, "isempty_  "));
        assertEquals("true", expansion.onRequest(null, "isempty_null"));
        assertEquals("true", expansion.onRequest(null, "isempty_NULL"));
        assertEquals("true", expansion.onRequest(null, "isempty_none"));
        assertEquals("true", expansion.onRequest(null, "isempty_NONE"));

        assertEquals("false", expansion.onRequest(null, "isempty_hello"));
        assertEquals("false", expansion.onRequest(null, "isempty_123"));
    }

    @Test
    void testIfEmptyPlaceholder() {
        assertEquals("empty", expansion.onRequest(null, "ifempty__empty_notempty"));
        assertEquals("empty", expansion.onRequest(null, "ifempty_null_empty_notempty"));
        assertEquals("notempty", expansion.onRequest(null, "ifempty_value_empty_notempty"));
    }

    @Test
    void testIsEqualPlaceholder() {
        assertEquals("yes", expansion.onRequest(null, "isequal_abc_ABC_yes_no"));
        assertEquals("no", expansion.onRequest(null, "isequal_abc_def_yes_no"));
    }

    @Test
    void testGreaterAndLessPlaceholder() {
        assertEquals("yes", expansion.onRequest(null, "greater_10_5_yes_no"));
        assertEquals("no", expansion.onRequest(null, "greater_5_10_yes_no"));

        assertEquals("yes", expansion.onRequest(null, "less_5_10_yes_no"));
        assertEquals("no", expansion.onRequest(null, "less_10_5_yes_no"));
    }

    @Test
    void testBetweenPlaceholder() {
        assertEquals("in range", expansion.onRequest(null, "between_5_1_10_in range_out of range"));
        assertEquals("out of range", expansion.onRequest(null, "between_15_1_10_in range_out of range"));
    }

    @Test
    void testContainsStartsWithEndsWithPlaceholder() {
        assertEquals("yes", expansion.onRequest(null, "contains_hello world_WORLD_yes_no"));
        assertEquals("no", expansion.onRequest(null, "contains_hello world_foo_yes_no"));

        assertEquals("yes", expansion.onRequest(null, "startswith_hello world_HEL_yes_no"));
        assertEquals("no", expansion.onRequest(null, "startswith_hello world_world_yes_no"));

        assertEquals("yes", expansion.onRequest(null, "endswith_hello world_RLD_yes_no"));
        assertEquals("no", expansion.onRequest(null, "endswith_hello world_hello_yes_no"));
    }

    @Test
    void testInvalidParams() {
        assertNull(expansion.onRequest(null, ""));
        assertNull(expansion.onRequest(null, "invalid"));
        assertNull(expansion.onRequest(null, "isempty")); // missing underscore
        assertNull(expansion.onRequest(null, "isempty_arg1_arg2")); // wrong arg count
    }
}
