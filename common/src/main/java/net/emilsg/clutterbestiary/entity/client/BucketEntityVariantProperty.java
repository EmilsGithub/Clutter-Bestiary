package net.emilsg.clutterbestiary.entity.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

/**
 * Client item select property that reads a string field (such as {@code Variant}) from the stored bucket entity data,
 * replacing the numeric model predicates used for bucket and bottle variants before client item definitions existed.
 */
public record BucketEntityVariantProperty(String field) implements SelectItemModelProperty<String> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "bucket_entity_variant");
    public static final Codec<String> VALUE_CODEC = Codec.STRING;
    public static final SelectItemModelProperty.Type<BucketEntityVariantProperty, String> TYPE = SelectItemModelProperty.Type.create(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("field", "Variant").forGetter(BucketEntityVariantProperty::field)
            ).apply(instance, BucketEntityVariantProperty::new)),
            VALUE_CODEC
    );

    @Nullable
    @Override
    public String get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        CustomData data = itemStack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (data == null) return null;
        return data.copyTag().getString(this.field).orElse(null);
    }

    @Override
    public SelectItemModelProperty.Type<BucketEntityVariantProperty, String> type() {
        return TYPE;
    }

    @Override
    public Codec<String> valueCodec() {
        return VALUE_CODEC;
    }
}
