package com.californium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class CaliforniumConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("californium.json");

    public int targetFps = 144;
    public boolean adaptiveRenderDistance = true;
    public boolean adaptiveEntities = true;
    public boolean adaptiveParticles = true;
    public boolean adaptiveEffects = true;
    public boolean adaptiveChunkBudget = true;
    public boolean showHud = true;
    public Preset preset = Preset.BALANCED;

    public enum Preset {
        EXTREME, BALANCED, QUALITY
    }

    public static CaliforniumConfig load() {
        if (!Files.exists(CONFIG_PATH)) {
            CaliforniumConfig config = new CaliforniumConfig();
            config.save();
            return config;
        }

        try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
            CaliforniumConfig cfg = GSON.fromJson(r, CaliforniumConfig.class);
            return cfg == null ? new CaliforniumConfig() : cfg;
        } catch (IOException e) {
            return new CaliforniumConfig();
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, w);
            }
        } catch (IOException ignored) {
        }
    }

    public void applyPreset(Preset selected) {
        this.preset = selected;
        switch (selected) {
            case EXTREME -> {
                targetFps = 240;
                adaptiveRenderDistance = true;
                adaptiveEntities = true;
                adaptiveParticles = true;
                adaptiveEffects = true;
                adaptiveChunkBudget = true;
            }
            case BALANCED -> {
                targetFps = 144;
                adaptiveRenderDistance = true;
                adaptiveEntities = true;
                adaptiveParticles = true;
                adaptiveEffects = true;
                adaptiveChunkBudget = true;
            }
            case QUALITY -> {
                targetFps = 90;
                adaptiveRenderDistance = false;
                adaptiveEntities = true;
                adaptiveParticles = false;
                adaptiveEffects = false;
                adaptiveChunkBudget = true;
            }
        }
    }
}
