package net.emilsg.clutterbestiary.mixin;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.util.datafix.schemas.V3938;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Makes the vanilla data fixer aware of Bestiary entity ids.
 * <p>
 * Entity chunks are fixed as one typed value, so a single unknown entity id makes the whole chunk fail to parse and
 * the fixer silently returns it unchanged - vanilla entities in the same chunk included. Registering our ids in the
 * schema used by 1.21.1 (inherited by every later schema) lets worlds from older versions upgrade normally.
 * Keep this list in sync with {@link net.emilsg.clutterbestiary.entity.ModEntityTypes}.
 */
@Mixin(V3938.class)
public abstract class EntityDataFixSchemaMixin {

    private static final List<String> CLUTTERBESTIARY$SIMPLE_ENTITIES = List.of(
            "butterfly", "butterfly_larva", "chameleon", "echofin", "mossbloom", "kiwi_bird", "emperor_penguin", "beaver",
            "capybara", "crimson_newt", "warped_newt", "ember_tortoise", "jellyfish", "manta_ray", "seahorse", "potion_wasp",
            "potion_sac", "dragonfly", "booplet", "koi", "koi_eggs", "river_turtle", "coati", "red_panda", "stoat", "crocodile",
            "chorus_beetle", "woodpecker", "arrowfish"
    );

    @Inject(method = "registerEntities", at = @At("RETURN"))
    private void clutterbestiary$registerEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
        Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();
        for (String name : CLUTTERBESTIARY$SIMPLE_ENTITIES) {
            schema.registerSimple(map, ClutterBestiary.MOD_ID + ":" + name);
        }
        // Arrowfish projectiles are AbstractArrows, so they share the arrow template (inBlockState, item, weapon).
        map.put(ClutterBestiary.MOD_ID + ":arrowfish_projectile", map.get("minecraft:arrow"));
    }
}
