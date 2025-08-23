package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WarpedNewtEntity extends AbstractNetherNewtEntity {

    public WarpedNewtEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ModBlockTags.WARPED_NEWTS_SPAWN_ON);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.WARPED_NEWT.create(world);
    }

    @Override
    protected Item getTamingItem() {
        return Items.TWISTING_VINES;
    }

    @Override
    public Item getBreedingItem() {
        return Items.WARPED_ROOTS;
    }

    @Override
    public RegistryEntry<StatusEffect> getOnAttackEffect() {
        return StatusEffects.POISON;
    }

    @Override
    protected Item getFungusItem() {
        return Items.WARPED_FUNGUS;
    }
}
