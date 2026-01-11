package net.emilsg.clutterbestiary.block.custom;

import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

import java.util.function.Supplier;

public class CrocodileEggBlock extends HatchingEggBlock {
    public static final IntProperty EGGS = IntProperty.of("eggs", 1, 3);

    public CrocodileEggBlock(Settings settings, Supplier<? extends EntityType<?>> type, float averageHatchTimeInMinutes, TagKey<Block> hatchBoostTag, double height, double width) {
        super(settings, type, averageHatchTimeInMinutes, hatchBoostTag, height, width);
        this.setDefaultState(this.getDefaultState().with(EGGS, 1));
    }

    @Override
    protected int getHatchlingCount(BlockState state) {
        return state.get(EGGS);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(EGGS);
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(EGGS) < 3) || super.canReplace(state, context);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());

        return state.isOf(this) ? state.with(EGGS, Math.min(3, state.get(EGGS) + 1)) : super.getPlacementState(context);
    }
}
