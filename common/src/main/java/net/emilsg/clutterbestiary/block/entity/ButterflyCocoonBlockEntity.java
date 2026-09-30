package net.emilsg.clutterbestiary.block.entity;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ButterflyCocoonBlockEntity extends BlockEntity {
    @Nullable
    private ButterflyVariant parentVariant;

    public ButterflyCocoonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BUTTERFLY_COCOON.get(), pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        this.parentVariant = nbt.getString("ParentVariant").map(ButterflyVariant::fromId).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        if (this.parentVariant != null) nbt.putString("ParentVariant", this.parentVariant.getId());
    }

    @Nullable
    public ButterflyVariant getParentVariant() {
        return this.parentVariant;
    }

    public void setParentVariant(ButterflyVariant variant) {
        this.parentVariant = variant;
        this.setChanged();
    }
}
