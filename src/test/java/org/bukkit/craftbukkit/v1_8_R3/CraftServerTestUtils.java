package org.bukkit.craftbukkit.v1_8_R3;

import org.bukkit.Server;
import org.mockito.Mockito;

import java.util.logging.Logger;

public class CraftServerTestUtils {

    public static Server createMockServer() {
        Server server = Mockito.mock(Server.class);
        Mockito.when(server.getLogger()).thenReturn(Logger.getLogger("Minecraft"));
        Mockito.when(server.getItemFactory()).thenReturn(Mockito.mock(org.bukkit.inventory.ItemFactory.class));
        return server;
    }
}
