package com.californium.mixin;

import com.californium.CaliforniumClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityDistanceTickMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void californium$throttleDistantTicks(CallbackInfo ci) {
        if (!CaliforniumClient.config().adaptiveEntities) {
            return;
        }

        Entity self = (Entity) (Object) this;
        if (self instanceof PlayerEntity) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }

        double distSq = self.squaredDistanceTo(mc.player);
        if (distSq < 96 * 96) {
            return;
        }

        if ((self.age & 0b11) != 0) {
            ci.cancel();
        }
    }
}
