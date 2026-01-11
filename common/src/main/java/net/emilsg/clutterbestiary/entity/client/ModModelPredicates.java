package net.emilsg.clutterbestiary.entity.client;

import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.BestiaryElytraItem;
import net.emilsg.clutterbestiary.item.custom.ButterflyBottleItem;
import net.emilsg.clutterbestiary.item.custom.JellyfishBucketItem;
import net.emilsg.clutterbestiary.item.custom.KoiBucketItem;
import net.emilsg.clutterbestiary.item.custom.RiverTurtleBucketItem;
import net.emilsg.clutterbestiary.item.custom.SeahorseBucketItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;

public final class ModModelPredicates {

    private ModModelPredicates() {
    }

    public static float getCrossbowArrowfish(ItemStack stack) {
        ChargedProjectilesComponent projectiles = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        return projectiles.contains(ModItems.ARROWFISH.get()) ? 1.0f : 0.0f;
    }

    public static float getElytraBroken(ItemStack stack) {
        if (!(stack.getItem() instanceof BestiaryElytraItem bestiaryElytraItem)) return 0.0f;
        return bestiaryElytraItem.isBroken(stack) ? 1.0f : 0.0f;
    }

    public static float getButterflyType(ItemStack stack) {
        if (!(stack.getItem() instanceof ButterflyBottleItem)) return 0.0f;
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        return entityData.get(ButterflyBottleItem.BUTTERFLY_VARIANT_MAP_CODEC).result()
                .map(variant -> (float) variant.getOrderedID() / 100)
                .orElse(0.0f);
    }

    public static float getSeahorseType(ItemStack stack) {
        if (!(stack.getItem() instanceof SeahorseBucketItem)) return 0.0f;
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        return entityData.get(SeahorseBucketItem.SEAHORSE_VARIANT_MAP_CODEC).result().map(variant -> switch (variant) {
            case LIGHT_BLUE -> 0.1f;
            case RED -> 0.2f;
            case PURPLE -> 0.3f;
            default -> 0.0f;
        }).orElse(0.0f);
    }

    public static float getKoiType(ItemStack stack) {
        if (!(stack.getItem() instanceof KoiBucketItem)) return 0.0f;
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        return entityData.get(KoiBucketItem.BASE_COLOR_CODEC).result().map(variant -> switch (variant) {
            case ORANGE -> 0.1f;
            case YELLOW -> 0.2f;
            case BLACK -> 0.3f;
            case PEARL -> 0.4f;
            default -> 0.0f;
        }).orElse(0.0f);
    }

    public static float getRiverTurtleType(ItemStack stack) {
        if (!(stack.getItem() instanceof RiverTurtleBucketItem)) return 0.0f;
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        return entityData.get(RiverTurtleBucketItem.RIVER_TURTLE_VARIANT_MAP_CODEC).result()
                .map(variant -> variant == RiverTurtleVariant.COCONUT ? 0.1f : 0.0f)
                .orElse(0.0f);
    }

    public static float getJellyfishType(ItemStack stack) {
        if (!(stack.getItem() instanceof JellyfishBucketItem)) return 0.0f;
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        return entityData.get(JellyfishBucketItem.JELLYFISH_VARIANT_MAP_CODEC).result().map(variant -> switch (variant) {
            case BLUE -> 0.1f;
            case PURPLE -> 0.2f;
            default -> 0.0f;
        }).orElse(0.0f);
    }
}
