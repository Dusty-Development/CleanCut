package net.dustley.clean_cut.item.cleaver

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.item.ModComponents
import net.dustley.clean_cut.util.MialeeText
import net.minecraft.client.gui.screen.Screen
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Style.EMPTY
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class CarionCleaverItem : SwordItem(ToolMaterials.NETHERITE, Settings().attributeModifiers(ATTRIBUTE_MODIFIERS))
{

    override fun postDamageEntity(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        super.postDamageEntity(stack, target, attacker)

        var chargeAmount = BLOOD_GAIN_HIT
        if(target.isDead) chargeAmount += BLOOD_GAIN_KILL
        if(attacker.fallDistance >= 0.01f) chargeAmount += BLOOD_GAIN_CRIT

        setBloodCharge(stack, getBloodCharge(stack) + chargeAmount)

        if(attacker.fallDistance >= 0.01f) onCrit(stack, target, attacker)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)

        if(getBloodCharge(stack) >= BLOOD_ABILITY_USAGE) {
            setBloodCharge(stack, getBloodCharge(stack) - BLOOD_ABILITY_USAGE)
            user.playSound(SoundEvents.ITEM_TOTEM_USE)
            onAbility(stack, world, user, hand)
        }
        return super.use(world, user, hand)
    }

    private fun onAbility(stack: ItemStack, world: World, user: PlayerEntity, hand: Hand) {

    }

    private fun onCrit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {

    }

    override fun getDefaultStack(): ItemStack {
        val stack = ItemStack(this)
        setBloodCharge(stack, 0.0f)
        return stack
    }

    private fun getBloodCharge(stack: ItemStack): Float { return stack.get(ModComponents.BLOOD_CHARGE) ?: 0.0f }
    private fun setBloodCharge(stack: ItemStack, value: Float) { stack.set(ModComponents.BLOOD_CHARGE, Math.clamp(value, 0f, 1f)) }

    // VISUALS //
    override fun isItemBarVisible(stack: ItemStack): Boolean { return getBloodCharge(stack) < BLOOD_ABILITY_USAGE }
    override fun getItemBarStep(stack: ItemStack): Int = Math.round(getBloodCharge(stack) * 13.0).toInt()
    override fun getItemBarColor(stack: ItemStack): Int = BLOOD_UI_COLOR.substring(1).toInt(16) // Convert hex to int

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        tooltip.add(Text.translatable("tooltip.${CleanCut.MOD_ID}.carion_cleaver").setStyle(EMPTY.withColor(Formatting.GRAY)))

        val color = BLOOD_UI_COLOR.substring(1).toInt(16) // Convert hex to int
        val percentage = if(stack.get(ModComponents.BLOOD_CHARGE) != null) (stack.get(ModComponents.BLOOD_CHARGE)!! * 100f).toInt() else 0
        if(Screen.hasShiftDown()) tooltip.add(Text.literal("Blood charge percentage: ").setStyle(EMPTY.withColor(Formatting.GRAY)).append(MialeeText.withColor(Text.literal("$percentage%").setStyle(EMPTY), color)))

        super.appendTooltip(stack, context, tooltip, type)

        if(!Screen.hasShiftDown()) tooltip.add(Text.translatable("tooltip.${CleanCut.MOD_ID}.carion_cleaver.shift"))
    }

    companion object {
        val ATTRIBUTE_MODIFIERS: AttributeModifiersComponent = createAttributeModifiers(ToolMaterials.NETHERITE, 3.5f, -2.4f)

        val BLOOD_UI_COLOR = "#de0d2b"

        val BLOOD_GAIN_KILL = 0.42f // How much of the blood is gained when killing an entity
        val BLOOD_GAIN_CRIT = 0.09f // How much of the blood is gained when crit an entity
        val BLOOD_GAIN_HIT = 0.01f // How much of the blood is gained when hitting an entity
        val BLOOD_ABILITY_USAGE = 1.0f // How much of the blood is used up when using an ability

        private fun createAttributeModifiers(material: ToolMaterial, baseAttackDamage: Float, attackSpeed: Float): AttributeModifiersComponent = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, (baseAttackDamage + material.attackDamage).toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND )
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed.toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build()
    }

}