package net.emilsg.clutterbestiary.block.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.block.entity.ButterflyCocoonBlockEntity;
import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ButterflyCocoonBlock extends BaseEntityBlock {
    public static final BooleanProperty CAN_HATCH = BooleanProperty.create("can_hatch");
    public static final IntegerProperty HATCH = IntegerProperty.create("hatch", 0, 3);
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(7, 15, 7, 9, 16, 9),
            Block.box(6, 10, 6, 10, 15, 10),
            Block.box(7, 9, 7, 9, 10, 9)
    );

    public ButterflyCocoonBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ButterflyCocoonBlockEntity(pos, state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.above()).getBlock() instanceof LeavesBlock || world.getBlockState(pos.above()).is(BlockTags.LOGS) || world.getBlockState(pos.above()).is(BlockTags.WART_BLOCKS) || world.getBlockState(pos.above()).is(Blocks.BONE_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (world.getBlockState(pos.above()).isAir()) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, world, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(200 + random.nextInt(200)) <= 0) {
            world.playLocalSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.MOSS_FALL, SoundSource.BLOCKS, 0.25f, 1.25f, false);
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (this.shouldHatchProgress(world, state)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                world.playSound(null, pos, SoundEvents.MOSS_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.setBlock(pos, state.setValue(HATCH, i + 1), 2);
            } else {
                ButterflyVariant variant = this.getButterFlyVariant(world, pos, random);
                world.playSound(null, pos, SoundEvents.MOSS_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.removeBlock(pos, false);
                if (random.nextInt(2) == 0)
                    popResource(world, pos, new ItemStack(ModItems.BUTTERFLY_ELYTRA_SMITHING_TEMPLATE_SHARDS.get()));

                for (int j = 0; j < 1; ++j) {
                    world.levelEvent(2001, pos, Block.getId(state));
                    ButterflyEntity butterflyEntity = ModEntityTypes.BUTTERFLY.get().create(world, EntitySpawnReason.BREEDING);
                    if (butterflyEntity != null) {
                        butterflyEntity.setAge(6000);
                        butterflyEntity.snapTo((double) pos.getX() + 0.3 + (double) j * 0.2, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.3, 0.0F, 0.0F);
                        butterflyEntity.setVariant(variant);
                        world.addFreshEntity(butterflyEntity);
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, CAN_HATCH);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack heldItem = player.getItemInHand(hand);
        if (world.isClientSide() && heldItem.is(Items.SHEARS) && state.getValue(CAN_HATCH)) {
            return InteractionResult.SUCCESS;
        }
        if (!world.isClientSide() && heldItem.is(Items.SHEARS) && state.getValue(CAN_HATCH)) {
            player.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
            if (!player.getAbilities().instabuild) {
                heldItem.hurtAndBreak(1, player, hand.asEquipmentSlot());
            }
            world.setBlock(pos, state.setValue(CAN_HATCH, false).setValue(HATCH, 0), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    private ButterflyVariant getButterFlyVariant(ServerLevel world, BlockPos pos, RandomSource random) {
        Holder<Biome> registryEntry = world.getBiome(pos);

        for (int i = 1; i <= 5; i++) {
            BlockPos checkPos = pos.below(i);
            Block block = world.getBlockState(checkPos).getBlock();

            if (ButterflyVariant.isVariantDecider(block)) {
                return ButterflyVariant.fromVariantDecider(block);
            }
        }

        ButterflyVariant parentVariant = world.getBlockEntity(pos) instanceof ButterflyCocoonBlockEntity cocoon
                ? cocoon.getParentVariant() : null;

        if (registryEntry.is(BiomeTags.IS_NETHER)) {
            if (registryEntry.is(Biomes.WARPED_FOREST)) return ButterflyVariant.WARPED;
            if (registryEntry.is(Biomes.CRIMSON_FOREST)) return ButterflyVariant.CRIMSON;
            if (registryEntry.is(Biomes.SOUL_SAND_VALLEY)) return ButterflyVariant.SOUL;

            if (parentVariant != null) return parentVariant;

            return switch (random.nextInt(3)) {
                case 0 -> ButterflyVariant.CRIMSON;
                case 1 -> ButterflyVariant.WARPED;
                default -> ButterflyVariant.SOUL;
            };
        }

        if (parentVariant != null) return parentVariant;

        if (registryEntry.is(BiomeTags.IS_OVERWORLD)) {
            return ButterflyVariant.getRandom(true);
        }

        return ButterflyVariant.WHITE;
    }

    private boolean shouldHatchProgress(Level world, BlockState state) {
        boolean isDay = world.isBrightOutside();
        boolean isNether = world.dimensionTypeRegistration().is(BuiltinDimensionTypes.NETHER);
        if ((isDay && state.getValue(CAN_HATCH)) || isNether) {
            return true;
        } else if (state.getValue(CAN_HATCH)) {
            return world.getRandom().nextInt(500) == 0;
        }
        return false;
    }
}
