package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;

public class CrocodileMateGoal extends AnimalMateGoal {
    private final CrocodileEntity crocodile;

    public CrocodileMateGoal(CrocodileEntity crocodile, double speed) {
        super(crocodile, speed);
        this.crocodile = crocodile;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !this.crocodile.hasEgg();
    }

    @Override
    protected void breed() {
        if (this.mate == null) return;

        ServerPlayerEntity player = this.animal.getLovingPlayer();
        if (player == null && this.mate.getLovingPlayer() != null) {
            player = this.mate.getLovingPlayer();
        }

        if (player != null) {
            player.incrementStat(Stats.ANIMALS_BRED);
            Criteria.BRED_ANIMALS.trigger(player, this.animal, this.mate, null);
        }

        this.crocodile.beginCarryingEgg();
        this.animal.setBreedingAge(6000);
        this.mate.setBreedingAge(6000);
        this.animal.resetLoveTicks();
        this.mate.resetLoveTicks();
        Random random = this.animal.getRandom();
        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
        }
    }
}
