package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.gamerules.GameRules;

public class CrocodileMateGoal extends BreedGoal {
    private final CrocodileEntity crocodile;

    public CrocodileMateGoal(CrocodileEntity crocodile, double speed) {
        super(crocodile, speed);
        this.crocodile = crocodile;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.crocodile.hasEgg();
    }

    @Override
    protected void breed() {
        if (this.partner == null) return;

        ServerPlayer player = this.animal.getLoveCause();
        if (player == null && this.partner.getLoveCause() != null) {
            player = this.partner.getLoveCause();
        }

        if (player != null) {
            player.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(player, this.animal, this.partner, null);
        }

        this.crocodile.beginCarryingEgg();
        this.animal.setAge(6000);
        this.partner.setAge(6000);
        this.animal.resetLove();
        this.partner.resetLove();
        RandomSource random = this.animal.getRandom();
        if (this.level.getGameRules().get(GameRules.MOB_DROPS)) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
        }
    }
}
