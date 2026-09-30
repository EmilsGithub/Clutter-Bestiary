package net.emilsg.clutterbestiary.fabric;

import net.emilsg.clutterbestiary.ClutterBestiaryClient;
import net.emilsg.clutterbestiary.entity.client.BucketEntityVariantProperty;
import net.emilsg.clutterbestiary.fabric.mixin.SelectItemModelPropertiesAccessor;
import net.fabricmc.api.ClientModInitializer;

public final class ClutterBestiaryClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClutterBestiaryClient.init();
        SelectItemModelPropertiesAccessor.clutterbestiary$getIdMapper().put(BucketEntityVariantProperty.ID, BucketEntityVariantProperty.TYPE);
    }

}
