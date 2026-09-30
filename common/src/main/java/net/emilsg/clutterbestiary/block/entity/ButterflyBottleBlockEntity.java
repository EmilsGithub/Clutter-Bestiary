package net.emilsg.clutterbestiary.block.entity;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ButterflyBottleBlockEntity extends BlockEntity {
    private CompoundTag butterflyData = new CompoundTag();

    public ButterflyBottleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BUTTERFLY_IN_A_BOTTLE.get(), pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveCustomOnly(registryLookup);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        if (butterflyData != null && !butterflyData.isEmpty()) {
            nbt.store("ButterflyData", CompoundTag.CODEC, butterflyData);
        }
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        butterflyData = nbt.read("ButterflyData", CompoundTag.CODEC).map(CompoundTag::copy).orElseGet(CompoundTag::new);
    }

    @Nullable
    public CompoundTag getButterflyData() {
        return butterflyData == null ? null : butterflyData.copy();
    }

    public void setButterflyData(@Nullable CompoundTag nbt) {
        this.butterflyData = nbt == null ? new CompoundTag() : nbt.copy();
        setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public Identifier getButterflyTexture() {
        if (butterflyData == null || butterflyData.isEmpty()) return null;

        if (butterflyData.contains("Variant")) {
            ButterflyVariant variant = ButterflyVariant.fromId(butterflyData.getStringOr("Variant", ""));
            return variant.getTextureLocation();
        }

        return null;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
