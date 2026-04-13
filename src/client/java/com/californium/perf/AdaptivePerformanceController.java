package com.californium.perf;

import com.californium.config.CaliforniumConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;

import java.util.ArrayDeque;
import java.util.Deque;

public class AdaptivePerformanceController {
    private final CaliforniumConfig config;
    private final Deque<Double> frameTimesMs = new ArrayDeque<>();

    private int renderDistance = 12;
    private int simulationDistance = 8;
    private int particleLimit = 8192;
    private int chunkUpdateBudget = 8;
    private double avgFps = 0.0;
    private double vanillaBaselineFps = -1;

    public AdaptivePerformanceController(CaliforniumConfig config) {
        this.config = config;
    }

    public void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) {
            return;
        }

        double frameTime = client.getRenderTickCounter().getTickTime();
        if (frameTime > 0) {
            frameTimesMs.add(frameTime);
        }
        while (frameTimesMs.size() > 120) {
            frameTimesMs.removeFirst();
        }

        avgFps = Math.max(1.0, 1000.0 / Math.max(0.1, frameTimesMs.stream().mapToDouble(d -> d).average().orElse(16.6)));

        if (vanillaBaselineFps < 0 && frameTimesMs.size() >= 100) {
            vanillaBaselineFps = avgFps;
        }

        int target = Math.max(30, config.targetFps);
        double pressure = target - avgFps;

        if (config.adaptiveRenderDistance) {
            if (pressure > 12) {
                renderDistance = Math.max(4, renderDistance - 1);
                simulationDistance = Math.max(4, simulationDistance - 1);
            } else if (pressure < -10) {
                renderDistance = Math.min(32, renderDistance + 1);
                simulationDistance = Math.min(renderDistance, simulationDistance + 1);
            }
            client.options.getViewDistance().setValue(renderDistance);
            client.options.getSimulationDistance().setValue(simulationDistance);
        }

        if (config.adaptiveParticles) {
            if (pressure > 15) {
                client.options.getParticles().setValue(net.minecraft.client.option.ParticlesMode.MINIMAL);
                particleLimit = Math.max(256, particleLimit - 512);
            } else if (pressure < -8) {
                client.options.getParticles().setValue(net.minecraft.client.option.ParticlesMode.ALL);
                particleLimit = Math.min(8192, particleLimit + 256);
            }
        }

        if (config.adaptiveEffects) {
            if (pressure > 14) {
                client.options.getCloudRenderMode().setValue(net.minecraft.client.option.CloudRenderMode.OFF);
                client.options.getGraphicsMode().setValue(GraphicsMode.FAST);
            } else if (pressure < -12) {
                client.options.getCloudRenderMode().setValue(net.minecraft.client.option.CloudRenderMode.FANCY);
            }
        }

        if (config.adaptiveChunkBudget) {
            if (pressure > 0) {
                chunkUpdateBudget = Math.max(2, chunkUpdateBudget - 1);
            } else {
                chunkUpdateBudget = Math.min(20, chunkUpdateBudget + 1);
            }
        }
    }

    public double getAvgFps() {
        return avgFps;
    }

    public double getVanillaBaselineFps() {
        return vanillaBaselineFps;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public int getChunkUpdateBudget() {
        return chunkUpdateBudget;
    }

    public int getParticleLimit() {
        return particleLimit;
    }
}
