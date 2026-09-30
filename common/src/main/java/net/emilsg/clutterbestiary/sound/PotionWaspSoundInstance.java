package net.emilsg.clutterbestiary.sound;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class PotionWaspSoundInstance extends AbstractTickableSoundInstance {
    protected final PotionWaspEntity potionWasp;

    public PotionWaspSoundInstance(PotionWaspEntity potionWasp) {
        super(ModSoundEvents.ENTITY_POTION_WASP_FLY.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.potionWasp = potionWasp;
        this.x = ((float) potionWasp.getX());
        this.y = ((float) potionWasp.getY());
        this.z = ((float) potionWasp.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    public boolean canPlaySound() {
        return !this.potionWasp.isSilent();
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        if (!this.potionWasp.isRemoved()) {
            this.x = ((float) this.potionWasp.getX());
            this.y = ((float) this.potionWasp.getY());
            this.z = ((float) this.potionWasp.getZ());
            float f = (float) this.potionWasp.getDeltaMovement().horizontalDistance();
            if (f >= 0.01F) {
                this.pitch = Mth.lerp(Mth.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }

        } else {
            this.stop();
        }
    }

    private float getMaxPitch() {
        return this.potionWasp.isBaby() ? 1.5F : 1.1F;
    }

    private float getMinPitch() {
        return this.potionWasp.isBaby() ? 1.1F : 0.7F;
    }
}
