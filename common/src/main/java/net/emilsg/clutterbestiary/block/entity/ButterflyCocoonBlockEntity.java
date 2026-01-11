package net.emilsg.clutterbestiary.block.entity;

import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ButterflyCocoonBlockEntity extends BlockEntity {
    @Nullable
    private ButterflyVariant parentVariant;

    public ButterflyCocoonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BUTTERFLY_COCOON.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.parentVariant = nbt.contains("ParentVariant") ? ButterflyVariant.fromId(nbt.getString("ParentVariant")) : null;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (this.parentVariant != null) nbt.putString("ParentVariant", this.parentVariant.getId());
    }

    @Nullable
    public ButterflyVariant getParentVariant() {
        return this.parentVariant;
    }

    public void setParentVariant(ButterflyVariant variant) {
        this.parentVariant = variant;
        this.markDirty();
    }
}
