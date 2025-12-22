package net.emilsg.clutterbestiary.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ClutterBestiary.MOD_ID, RegistryKeys.SOUND_EVENT);

    public static final Supplier<SoundEvent> ENTITY_KIWI_CALL = registerSoundEvent("entity_kiwi_call");
    public static final Supplier<SoundEvent> ENTITY_NETHER_NEWT_HURT = registerSoundEvent("entity_nether_newt_hurt");
    public static final Supplier<SoundEvent> ENTITY_NETHER_NEWT_AMBIENT = registerSoundEvent("entity_nether_newt_ambient");
    public static final Supplier<SoundEvent> ENTITY_POTION_WASP_FLY = registerSoundEvent("entity_potion_wasp_fly");
    public static final Supplier<SoundEvent> ENTITY_BOOPLET_SQUEAK = registerSoundEvent("entity_booplet_squeak");
    public static final Supplier<SoundEvent> ENTITY_MOSSBLOOM_HURT = registerSoundEvent("entity_mossbloom_hurt");
    public static final Supplier<SoundEvent> ENTITY_EMPEROR_PENGUIN_AMBIENT = registerSoundEvent("entity_emperor_penguin_ambient");
    public static final Supplier<SoundEvent> ENTITY_EMBER_TORTOISE_HURT = registerSoundEvent("entity_ember_tortoise_hurt");
    public static final Supplier<SoundEvent> ENTITY_COATI_AMBIENT = registerSoundEvent("entity_coati_ambient");
    public static final Supplier<SoundEvent> ENTITY_BEAVER_CHAINSAW = registerSoundEvent("entity_beaver_chainsaw");
    public static final Supplier<SoundEvent> ENTITY_RIVER_TURTLE_AMBIENT = registerSoundEvent("entity_river_turtle_ambient");
    public static final Supplier<SoundEvent> ENTITY_RIVER_TURTLE_HURT = registerSoundEvent("entity_river_turtle_hurt");

    public static RegistrySupplier<SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.of(Identifier.of(ClutterBestiary.MOD_ID, name)));
    }

    public static void register() {
        SOUNDS.register();
    }
}
