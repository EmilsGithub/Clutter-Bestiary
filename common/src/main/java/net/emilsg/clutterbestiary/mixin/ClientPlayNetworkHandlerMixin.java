package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.emilsg.clutterbestiary.sound.PotionWaspSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "postAddEntitySoundInstance", at = @At("HEAD"))
    private void clutterbestiary$playPotionWaspSpawnSound(Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof PotionWaspEntity potionWasp) {
            Minecraft client = ((ClientCommonNetworkHandlerAccessor) this).getClient();
            client.getSoundManager().queueTickingSound(new PotionWaspSoundInstance(potionWasp));
        }
    }
}
