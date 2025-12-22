package net.emilsg.clutterbestiary.block.custom;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.block.ICutoutRenderable;
import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ButterflyBottleBlock extends HorizontalFacingBlock implements BlockEntityProvider, ICutoutRenderable {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2.5, 0, 2.5, 13.5, 11.5, 13.5),
            Block.createCuboidShape(4.5, 11, 4.5, 11.5, 12, 11.5),
            Block.createCuboidShape(3.5, 11.5, 3.5, 12.5, 15.5, 12.5),
            Block.createCuboidShape(4.5, 15, 4.5, 11.5, 16, 11.5)
    );

    public ButterflyBottleBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
        super.afterBreak(world, player, pos, state, be, tool);

        if (!world.isClient && be instanceof ButterflyBottleBlockEntity bottleBe) {
            ItemStack stack = new ItemStack(ModItems.BUTTERFLY_IN_A_BOTTLE.get());

            NbtCompound data = bottleBe.getButterflyData();
            if (data != null) {
                NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, data);
            }

            dropStack(world, pos, stack);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ButterflyBottleBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return createCodec(ButterflyBottleBlock::new);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
