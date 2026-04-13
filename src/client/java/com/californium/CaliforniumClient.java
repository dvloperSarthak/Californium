package com.californium;

import com.californium.config.CaliforniumConfig;
import com.californium.gui.CaliforniumConfigScreen;
import com.californium.perf.AdaptivePerformanceController;
import com.californium.perf.PerformanceHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CaliforniumClient implements ClientModInitializer {
    public static final String MOD_ID = "californium";

    private static final CaliforniumConfig CONFIG = CaliforniumConfig.load();
    private static final AdaptivePerformanceController CONTROLLER = new AdaptivePerformanceController(CONFIG);

    public static CaliforniumConfig config() { return CONFIG; }
    public static AdaptivePerformanceController controller() { return CONTROLLER; }

    @Override
    public void onInitializeClient() {
        PerformanceHud.register(CONFIG, CONTROLLER);

        KeyBinding openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.californium.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.californium"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            CONTROLLER.tick(client);
            while (openConfig.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setScreen(new CaliforniumConfigScreen(mc.currentScreen, CONFIG));
            }
        });
    }
}
