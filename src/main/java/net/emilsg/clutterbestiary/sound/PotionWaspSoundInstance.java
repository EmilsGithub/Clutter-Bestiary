package net.emilsg.clutterbestiary.sound;

import net.emilsg.clutterbestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class PotionWaspSoundInstance extends MovingSoundInstance {
    protected final PotionWaspEntity potionWasp;

    public PotionWaspSoundInstance(PotionWaspEntity potionWasp) {
        super(ModSoundEvents.ENTITY_POTION_WASP_FLY, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.potionWasp = potionWasp;
        this.x = ((float)potionWasp.getX());
        this.y = ((float)potionWasp.getY());
        this.z = ((float)potionWasp.getZ());
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.0F;
    }

    public boolean canPlay() {
        return !this.potionWasp.isSilent();
    }

    public boolean shouldAlwaysPlay() {
        return true;
    }

    public void tick() {
        if (!this.potionWasp.isRemoved()) {
            this.x = ((float)this.potionWasp.getX());
            this.y = ((float)this.potionWasp.getY());
            this.z = ((float)this.potionWasp.getZ());
            float f = (float)this.potionWasp.getVelocity().horizontalLength();
            if (f >= 0.01F) {
                this.pitch = MathHelper.lerp(MathHelper.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }

        } else {
            this.setDone();
        }
    }

    private float getMaxPitch() {
        return this.potionWasp.isBaby() ? 1.5F : 1.1F;
    }

    private float getMinPitch() {
        return this.potionWasp.isBaby() ? 1.1F : 0.7F;
    }
}
