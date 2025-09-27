package net.emilsg.clutterbestiary.entity.client.render;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.emilsg.clutterbestiary.item.custom.ButterflyElytraItem;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModElytraRenderer<E extends LivingEntity, M extends EntityModel<E>> extends ElytraFeatureRenderer<E, M> {
    private final ElytraEntityModel<E> model;

    public ModElytraRenderer(FeatureRendererContext<E, M> context, EntityModelLoader loader) {
        super(context, loader);
        this.model = new ElytraEntityModel<>(loader.getModelPart(EntityModelLayers.ELYTRA));
    }

    @Override
    protected Identifier getTexture(E entity) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        System.out.println(entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ButterflyElytraItem);
        if(!(itemStack.getItem() instanceof ButterflyElytraItem butterflyElytraItem)) return super.getTexture(entity);
        return Identifier.of(ClutterBestiary.MOD_ID, "textures/entity/elytra/" + butterflyElytraItem.getType() + ".png");
    }
}
