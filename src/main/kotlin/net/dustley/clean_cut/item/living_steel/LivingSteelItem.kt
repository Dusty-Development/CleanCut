package net.dustley.clean_cut.item.living_steel

import net.dustley.clean_cut.item.ModItemComponents
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Style.EMPTY
import net.minecraft.text.Text
import net.minecraft.util.Formatting


class LivingSteelItem : Item(Settings().food(ModItemComponents.LIVING_STEEL_FOOD)) {

    override fun appendTooltip(
        stack: ItemStack?,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        type: TooltipType?,
    ) {
        tooltip.add(Text.translatable("tooltip.clean_cut.living_steel.tooltip").setStyle(EMPTY.withColor(Formatting.DARK_GRAY)))
        super.appendTooltip(stack, context, tooltip, type)
    }

}