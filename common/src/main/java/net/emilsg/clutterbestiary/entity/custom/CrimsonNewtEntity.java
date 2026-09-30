package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class CrimsonNewtEntity extends AbstractNetherNewtEntity {
    private static final Item BREEDING_ITEM = Items.CRIMSON_ROOTS;
    private static final Item TAMING_ITEM = Items.WEEPING_VINES;

    public CrimsonNewtEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.CRIMSON_NEWTS_SPAWN_ON);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.CRIMSON_NEWT.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public Item getBreedingItem() {
        return BREEDING_ITEM;
    }

    @Override
    public Holder<MobEffect> getOnAttackEffect() {
        //TODO add status effect when vulnerability has released: Registries.STATUS_EFFECT.get(Identifier.of("EXTERNAL_MOD_ID", "vulnerability"))
        return null;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    @Override
    protected Item getFungusItem() {
        return Items.CRIMSON_FUNGUS;
    }
}
