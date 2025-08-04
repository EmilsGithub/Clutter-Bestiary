package net.emilsg.clutterbestiary.sound;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent ENTITY_KIWI_CALL = registerSoundEvent("entity_kiwi_call");
    public static final SoundEvent ENTITY_NETHER_NEWT_HURT = registerSoundEvent("entity_nether_newt_hurt");
    public static final SoundEvent ENTITY_NETHER_NEWT_AMBIENT = registerSoundEvent("entity_nether_newt_ambient");
    public static final SoundEvent ENTITY_POTION_WASP_FLY = registerSoundEvent("entity_potion_wasp_fly");
    public static final SoundEvent ENTITY_BOOPLET_SQUEAK = registerSoundEvent("entity_booplet_squeak");
    public static final SoundEvent ENTITY_MOSSBLOOM_HURT = registerSoundEvent("entity_mossbloom_hurt");
    public static final SoundEvent ENTITY_EMPEROR_PENGUIN_AMBIENT = registerSoundEvent("entity_emperor_penguin_ambient");
    public static final SoundEvent ENTITY_EMBER_TORTOISE_HURT = registerSoundEvent("entity_ember_tortoise_hurt");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(ClutterBestiary.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {

    }
}
