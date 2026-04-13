package com.californium.gui;

import com.californium.config.CaliforniumConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CaliforniumConfigScreen extends Screen {
    private final Screen parent;
    private final CaliforniumConfig config;

    public CaliforniumConfigScreen(Screen parent, CaliforniumConfig config) {
        super(Text.literal("Californium Settings"));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 4;

        addDrawableChild(ButtonWidget.builder(Text.literal("Preset: " + config.preset), b -> {
            CaliforniumConfig.Preset[] values = CaliforniumConfig.Preset.values();
            int next = (config.preset.ordinal() + 1) % values.length;
            config.applyPreset(values[next]);
            config.save();
            b.setMessage(Text.literal("Preset: " + config.preset));
        }).dimensions(x, y, 200, 20).build());

        y += 24;
        addDrawableChild(ButtonWidget.builder(Text.literal("Target FPS: " + config.targetFps), b -> {
            int[] cycle = {60, 90, 120, 144, 165, 240};
            int idx = 0;
            for (int i = 0; i < cycle.length; i++) {
                if (cycle[i] == config.targetFps) {
                    idx = i;
                    break;
                }
            }
            config.targetFps = cycle[(idx + 1) % cycle.length];
            config.save();
            b.setMessage(Text.literal("Target FPS: " + config.targetFps));
        }).dimensions(x, y, 200, 20).build());

        y += 24;
        addDrawableChild(toggle(x, y, "Adaptive Render Distance", () -> config.adaptiveRenderDistance, v -> config.adaptiveRenderDistance = v));
        y += 24;
        addDrawableChild(toggle(x, y, "Adaptive Entities", () -> config.adaptiveEntities, v -> config.adaptiveEntities = v));
        y += 24;
        addDrawableChild(toggle(x, y, "Adaptive Particles", () -> config.adaptiveParticles, v -> config.adaptiveParticles = v));
        y += 24;
        addDrawableChild(toggle(x, y, "Adaptive Effects", () -> config.adaptiveEffects, v -> config.adaptiveEffects = v));
        y += 24;
        addDrawableChild(toggle(x, y, "Adaptive Chunk Budget", () -> config.adaptiveChunkBudget, v -> config.adaptiveChunkBudget = v));
        y += 24;
        addDrawableChild(toggle(x, y, "HUD", () -> config.showHud, v -> config.showHud = v));

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> {
            config.save();
            this.client.setScreen(parent);
        }).dimensions(x, this.height - 28, 200, 20).build());
    }

    private ButtonWidget toggle(int x, int y, String label, BoolGetter getter, BoolSetter setter) {
        return ButtonWidget.builder(message(label, getter.get()), b -> {
            setter.set(!getter.get());
            config.save();
            b.setMessage(message(label, getter.get()));
        }).dimensions(x, y, 200, 20).build();
    }

    private Text message(String label, boolean value) {
        return Text.literal(label + ": " + (value ? "ON" : "OFF"));
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @FunctionalInterface
    private interface BoolGetter {
        boolean get();
    }

    @FunctionalInterface
    private interface BoolSetter {
        void set(boolean value);
    }
}
