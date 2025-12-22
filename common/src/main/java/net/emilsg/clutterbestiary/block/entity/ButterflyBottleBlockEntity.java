package net.emilsg.clutterbestiary.block.entity;

import net.emilsg.clutterbestiary.entity.variants.ButterflyVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ButterflyBottleBlockEntity extends BlockEntity {
    private NbtCompound butterflyData = new NbtCompound();

    public ButterflyBottleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BUTTERFLY_IN_A_BOTTLE.get(), pos, state);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt, registryLookup);
        return nbt;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (butterflyData != null && !butterflyData.isEmpty()) {
            nbt.put("ButterflyData", butterflyData);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("ButterflyData", NbtElement.COMPOUND_TYPE)) {
            butterflyData = nbt.getCompound("ButterflyData").copy();
        } else {
            butterflyData = new NbtCompound();
        }
    }

    @Nullable
    public NbtCompound getButterflyData() {
        return butterflyData == null ? null : butterflyData.copy();
    }

    public void setButterflyData(@Nullable NbtCompound nbt) {
        this.butterflyData = nbt == null ? new NbtCompound() : nbt.copy();
        markDirty();

        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public Identifier getButterflyTexture() {
        if (butterflyData == null || butterflyData.isEmpty()) return null;

        if (butterflyData.contains("Variant")) {
            ButterflyVariant variant = ButterflyVariant.fromId(butterflyData.getString("Variant"));
            return variant.getTextureLocation();
        }

        return null;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
