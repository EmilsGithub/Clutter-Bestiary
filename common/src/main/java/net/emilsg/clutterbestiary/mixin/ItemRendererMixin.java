package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.client.ArrowfishCrossbowModel;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private ItemModels models;

    @Inject(method = "getModel", at = @At("RETURN"), cancellable = true)
    private void clutterbestiary$useArrowfishCrossbowModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> callbackInfo) {
        BakedModel arrowfishModel = ArrowfishCrossbowModel.getModel(stack, this.models.getModelManager());
        if (arrowfishModel != null) callbackInfo.setReturnValue(arrowfishModel);
    }
}
