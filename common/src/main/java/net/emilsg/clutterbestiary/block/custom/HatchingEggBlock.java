package net.emilsg.clutterbestiary.block.custom;

import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HatchingEggBlock extends Block {
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    private final Supplier<? extends EntityType<?>> type;
    private final float averageHatchTimeInMinutes;
    private final TagKey<Block> hatchBoostTag;
    private final double height;
    private final double width;

    public HatchingEggBlock(Properties settings, Supplier<? extends EntityType<?>> type, float averageHatchTimeInMinutes, @Nullable TagKey<Block> hatchBoostTag, double height, double width) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any()).setValue(HATCH, 0));
        this.type = type;
        this.averageHatchTimeInMinutes = averageHatchTimeInMinutes;
        this.hatchBoostTag = hatchBoostTag;
        this.height = height;
        this.width = width;
    }

    public int getHatchStage(BlockState state) {
        return state.getValue(HATCH);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (width <= 0 || height <= 0) return Shapes.block();
        return Block.box(8 - (width / 2), 0, 8 - (width / 2), 8 + (width / 2), height, 8 + (width / 2));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public boolean isAboveHatchBooster(BlockGetter world, BlockPos pos) {
        if (hatchBoostTag == null) return false;
        return world.getBlockState(pos.below()).is(hatchBoostTag);
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        boolean aboveHatchBooster = isAboveHatchBooster(world, pos);
        if (!world.isClientSide() && aboveHatchBooster) {
            world.levelEvent(3009, pos, 0);
        }
        var hatchTime = aboveHatchBooster ? (averageHatchTimeInMinutes * 600) : (averageHatchTimeInMinutes * 1200);
        int hatchEventTime = (int) hatchTime / 3;
        world.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(state));
        world.scheduleTick(pos, this, hatchEventTime + world.getRandom().nextInt(300));
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!this.isReadyToHatch(state)) {
            world.playSound(null, pos, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            world.setBlock(pos, state.setValue(HATCH, this.getHatchStage(state) + 1), 2);
            boolean aboveHatchBooster = this.isAboveHatchBooster(world, pos);
            float hatchTime = aboveHatchBooster ? this.averageHatchTimeInMinutes * 600 : this.averageHatchTimeInMinutes * 1200;
            world.scheduleTick(pos, this, (int) hatchTime / 3 + random.nextInt(300));
        } else {
            world.playSound(null, pos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            world.destroyBlock(pos, false);
            for (int i = 0; i < this.getHatchlingCount(state); i++) {
                Animal animalEntity = (Animal) type.get().create(world, EntitySpawnReason.BREEDING);
                if (animalEntity != null) {
                    Vec3 vec3d = Vec3.atCenterOf(pos);
                    animalEntity.setBaby(true);
                    animalEntity.snapTo(vec3d.x(), vec3d.y(), vec3d.z(), Mth.wrapDegrees(world.getRandom().nextFloat() * 360.0F), 0.0F);
                    world.addFreshEntity(animalEntity);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }

    private boolean isReadyToHatch(BlockState state) {
        return this.getHatchStage(state) == 2;
    }

    protected int getHatchlingCount(BlockState state) {
        return 1;
    }
}
