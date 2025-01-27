package net.dustley.clean_cut.item.food.living_steel;

import net.dustley.clean_cut.component.ModItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class LivingSteelItem extends Item {

    public LivingSteelItem() {
        super(new Settings().food(ModItemComponents.LIVING_STEEL_FOOD));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.clean_cut.living_steel.tooltip").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
