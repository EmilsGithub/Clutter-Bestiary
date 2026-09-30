package net.emilsg.clutterbestiary.block.custom;

import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import java.util.function.Supplier;

public class CrocodileEggBlock extends HatchingEggBlock {
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 3);

    public CrocodileEggBlock(Properties settings, Supplier<? extends EntityType<?>> type, float averageHatchTimeInMinutes, TagKey<Block> hatchBoostTag, double height, double width) {
        super(settings, type, averageHatchTimeInMinutes, hatchBoostTag, height, width);
        this.registerDefaultState(this.defaultBlockState().setValue(EGGS, 1));
    }

    @Override
    protected int getHatchlingCount(BlockState state) {
        return state.getValue(EGGS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EGGS);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return (!context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(EGGS) < 3) || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        return state.is(this) ? state.setValue(EGGS, Math.min(3, state.getValue(EGGS) + 1)) : super.getStateForPlacement(context);
    }
}
