package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class ButterflyElytraSmithingTemplateItem extends Item {
    private final Text ELYTRA_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ClutterBestiary.MOD_ID, "smithing_template.elytra_upgrade"))).formatted(Formatting.GRAY);
    private final Text ELYTRAS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ClutterBestiary.MOD_ID, "smithing_template.elytras"))).formatted(Formatting.BLUE);
    private final Text APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ClutterBestiary.MOD_ID, "smithing_template.applies_to"))).formatted(Formatting.GRAY);
    private final Text INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ClutterBestiary.MOD_ID, "smithing_template.ingredients"))).formatted(Formatting.GRAY);
    private final Text INGREDIENTS_USED_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ClutterBestiary.MOD_ID, "smithing_template.ingredients_used"))).formatted(Formatting.BLUE);


    public ButterflyElytraSmithingTemplateItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(ELYTRA_UPGRADE_TEXT);
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(APPLIES_TO_TEXT);
        tooltip.add(ScreenTexts.space().append(ELYTRAS_TEXT));
        tooltip.add(INGREDIENTS_TEXT);
        tooltip.add(ScreenTexts.space().append(INGREDIENTS_USED_TEXT));
    }
}
