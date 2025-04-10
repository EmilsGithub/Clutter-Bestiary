package net.emilsg.clutter_bestiary.sound;

import net.emilsg.clutter_bestiary.ClutterBestiary;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static SoundEvent ENTITY_KIWI_CALL = registerSoundEvent("entity_kiwi_call");
    public static SoundEvent ENTITY_NETHER_NEWT_HURT = registerSoundEvent("entity_nether_newt_hurt");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(ClutterBestiary.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {

    }
}
