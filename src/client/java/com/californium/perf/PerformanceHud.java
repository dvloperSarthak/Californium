package com.californium.perf;

import com.californium.config.CaliforniumConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Locale;

public final class PerformanceHud {
    private PerformanceHud() {}

    public static void register(CaliforniumConfig config, AdaptivePerformanceController controller) {
        HudRenderCallback.EVENT.register((DrawContext context, float tickDelta) -> {
            if (!config.showHud) {
                return;
            }

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.options.debugEnabled) {
                return;
            }

            int y = 8;
            int x = 8;
            context.drawText(mc.textRenderer, Text.literal("Californium"), x, y, 0x5ef5ff, true);
            y += 11;

            double fps = controller.getAvgFps();
            double baseline = controller.getVanillaBaselineFps();
            String ratio = baseline > 0 ? String.format(Locale.US, "x%.2f", fps / baseline) : "profiling...";
            context.drawText(mc.textRenderer, Text.literal(String.format(Locale.US, "FPS: %.1f (%s vs baseline)", fps, ratio)), x, y, 0xFFFFFF, true);
            y += 10;
            context.drawText(mc.textRenderer, Text.literal("RD " + controller.getRenderDistance() + " | ChunkBudget " + controller.getChunkUpdateBudget()), x, y, 0xFFFFFF, true);
            y += 10;
            context.drawText(mc.textRenderer, Text.literal("Particles cap: " + controller.getParticleLimit()), x, y, 0xFFFFFF, true);
        });
    }
}
