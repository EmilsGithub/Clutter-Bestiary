package net.emilsg.clutterbestiary.block.custom;

import net.emilsg.clutterbestiary.block.entity.ButterflyBottleBlockEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ButterflyBottleBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public final VoxelShape SHAPE = Shapes.or(
            Block.box(2.5, 0, 2.5, 13.5, 11.5, 13.5),
            Block.box(4.5, 11, 4.5, 11.5, 12, 11.5),
            Block.box(3.5, 11.5, 3.5, 12.5, 15.5, 12.5),
            Block.box(4.5, 15, 4.5, 11.5, 16, 11.5)
    );

    public ButterflyBottleBlock(Properties settings) {
        super(settings.noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void playerDestroy(ServerLevel world, ServerPlayer player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
        super.playerDestroy(world, player, pos, state, be, tool);

        if (!world.isClientSide() && be instanceof ButterflyBottleBlockEntity bottleBe) {
            ItemStack stack = new ItemStack(ModItems.BUTTERFLY_IN_A_BOTTLE.get());

            CompoundTag data = bottleBe.getButterflyData();
            if (data != null) {
                CustomData.set(DataComponents.BUCKET_ENTITY_DATA, stack, data);
            }
            stack.applyComponents(bottleBe.collectComponents());

            popResource(world, pos, stack);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ButterflyBottleBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
