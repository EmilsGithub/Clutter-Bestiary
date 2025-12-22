package net.emilsg.clutterbestiary.item.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RiverTurtleBucketItem extends BucketItem {
    public static final MapCodec<RiverTurtleVariant> RIVER_TURTLE_VARIANT_MAP_CODEC = RiverTurtleVariant.CODEC.fieldOf("Variant");
    private final Supplier<? extends EntityType<?>> type;
    private final SoundEvent emptyingSound;

    public RiverTurtleBucketItem(Supplier<? extends EntityType<?>> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
        super(fluid, settings);
        this.type = type;
        this.emptyingSound = emptyingSound;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.type == ModEntityTypes.RIVER_TURTLE) {
            NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            if (nbtComponent.isEmpty()) {
                return;
            }

            Optional<RiverTurtleVariant> optional = nbtComponent.get(RIVER_TURTLE_VARIANT_MAP_CODEC).result();
            if (optional.isPresent()) {
                RiverTurtleVariant variant = optional.get();

                Formatting formatting = variant.getColorFormatting();

                String string = "clutterbestiary." + variant.getName() + ".river_turtle";

                MutableText mutableText = Text.translatable(string);
                mutableText.formatted(formatting);
                tooltip.add(mutableText);
            }
        }
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerWorld) {
            this.spawnEntity((ServerWorld) world, stack, pos);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.NEUTRAL);
            if (player != null) {
                Hand hand = player.getActiveHand();
                player.setStackInHand(hand, getEmptiedStack(stack, player));
            }
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        world.playSound(player, pos, this.emptyingSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        RiverTurtleEntity riverTurtleEntity = ModEntityTypes.RIVER_TURTLE.get().spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        if (riverTurtleEntity != null) riverTurtleEntity.copyDataFromNbt(riverTurtleEntity, nbtComponent.copyNbt());
    }

}
