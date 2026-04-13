package com.californium.mixin;

import com.californium.CaliforniumClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class ParticleThrottleMixin {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void californium$limitParticles(Particle particle, CallbackInfo ci) {
        if (!CaliforniumClient.config().adaptiveParticles) {
            return;
        }

        double fps = CaliforniumClient.controller().getAvgFps();
        int target = CaliforniumClient.config().targetFps;
        if (fps < target * 0.8 && (System.nanoTime() & 1L) == 0L) {
            ci.cancel();
        }
    }
}
