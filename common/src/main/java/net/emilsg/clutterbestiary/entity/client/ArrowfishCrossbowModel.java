package net.emilsg.clutterbestiary.entity.client;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Swaps in the Arrowfish-loaded crossbow model without overriding the vanilla crossbow model file,
 * so resource packs that change the crossbow keep working. Each platform loads {@link #MODEL_ID} as an
 * extra model and supplies how to look it up.
 */
public final class ArrowfishCrossbowModel {
    public static final Identifier MODEL_ID = Identifier.of(ClutterBestiary.MOD_ID, "item/crossbow_arrowfish");

    private static Function<BakedModelManager, BakedModel> modelLookup = manager -> null;

    private ArrowfishCrossbowModel() {
    }

    public static void setModelLookup(Function<BakedModelManager, BakedModel> lookup) {
        modelLookup = lookup;
    }

    @Nullable
    public static BakedModel getModel(ItemStack stack, BakedModelManager manager) {
        if (!stack.isOf(Items.CROSSBOW) || ModModelPredicates.getCrossbowArrowfish(stack) == 0.0f) return null;

        BakedModel model = modelLookup.apply(manager);
        return model == null || model == manager.getMissingModel() ? null : model;
    }
}
