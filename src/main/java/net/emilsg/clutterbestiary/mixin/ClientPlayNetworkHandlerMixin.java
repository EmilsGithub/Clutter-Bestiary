package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.emilsg.clutterbestiary.sound.PotionWaspSoundInstance;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Final
    @Shadow
    private net.minecraft.client.MinecraftClient client;

    @Inject(method = "playSpawnSound", at = @At("HEAD"))
    private void onPlaySpawnSound(Entity entity, CallbackInfo ci) {
        if (entity instanceof PotionWaspEntity potionWasp) {
            PotionWaspSoundInstance potionWaspSoundInstance = new PotionWaspSoundInstance(potionWasp);
            this.client.getSoundManager().playNextTick(potionWaspSoundInstance);
        }
    }
}
