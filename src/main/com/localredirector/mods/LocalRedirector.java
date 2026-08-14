package com.localredirector.mods;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.clientplugin.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import java.lang.reflect.Field;

public class LocalRedirector implements ClientModInitializer {
    private static final String TARGET_ADDRESS = "mc.craftmc.cn";

    @Override
    public void onInitializeClient() {
        ClientLoginConnectionEvents.INIT.register((client, handler, connectionInfo) -> {
            ServerAddress currentAddress = connectionInfo.address();
            String currentHost = currentAddress.getHost();
            if ("127.0.0.1".equals(currentHost)) {
                ServerAddress newAddress = new ServerAddress(TARGET_ADDRESS, 25565);
                try {
                    Field addressField = connectionInfo.getClass().getDeclaredField("address");
                    addressField.setAccessible(true);
                    addressField.set(connectionInfo, newAddress);
                } catch (Exception e) {
                    handler.disconnect(Component.literal("Redirect failed"));
                }
            }
        });
    }
}
