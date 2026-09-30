package net.emilsg.clutterbestiary.item.custom;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class ButterflyElytraSmithingTemplateItem extends Item {
    private final Component ELYTRA_UPGRADE_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "smithing_template.elytra_upgrade"))).withStyle(ChatFormatting.GRAY);
    private final Component ELYTRAS_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "smithing_template.elytras"))).withStyle(ChatFormatting.BLUE);
    private final Component APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "smithing_template.applies_to"))).withStyle(ChatFormatting.GRAY);
    private final Component INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "smithing_template.ingredients"))).withStyle(ChatFormatting.GRAY);
    private final Component INGREDIENTS_USED_TEXT = Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "smithing_template.ingredients_used"))).withStyle(ChatFormatting.BLUE);


    public ButterflyElytraSmithingTemplateItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, display, tooltip, type);
        tooltip.accept(ELYTRA_UPGRADE_TEXT);
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(APPLIES_TO_TEXT);
        tooltip.accept(CommonComponents.space().append(ELYTRAS_TEXT));
        tooltip.accept(INGREDIENTS_TEXT);
        tooltip.accept(CommonComponents.space().append(INGREDIENTS_USED_TEXT));
    }
}
