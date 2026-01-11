package net.emilsg.clutterbestiary.neoforge.spawns;

import com.mojang.serialization.MapCodec;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifierSerializers {

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ClutterBestiary.MOD_ID);

    @SuppressWarnings("unused")
    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ModBiomeModifier>> CLUTTER_BESTIARY_SPAWNS = REGISTER.register("spawns", () -> ModBiomeModifier.CODEC);
}