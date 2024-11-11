package net.dustley.clean_cut.item.cleaver

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.entity.thrown_cleaver.ThrownCleaverEntity
import net.dustley.clean_cut.item.ModItemComponents
import net.dustley.clean_cut.item.ModItems
import net.dustley.clean_cut.util.MialeeText
import net.minecraft.block.BlockState
import net.minecraft.client.gui.screen.Screen
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.ToolComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ProjectileItem
import net.minecraft.item.ToolMaterials
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Style.EMPTY
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class CarionCleaverItem : AxeItem(ToolMaterials.NETHERITE, createItemSettings()), ProjectileItem
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
        user.setCurrentHand(hand)
        return TypedActionResult.consume(stack)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        user.playSound(SoundEvents.BLOCK_NETHERRACK_HIT)
        setBloodCharge(stack, getBloodCharge(stack) - BLOOD_ABILITY_USAGE)
        onAbility(stack, world, user, getBloodCharge(stack))
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    private fun onAbility(stack: ItemStack, world: World, user: LivingEntity, power:Float) {
        val entity = ThrownCleaverEntity(user, world, 2.5, 1.0, stack.copyAndEmpty())
        world.spawnEntity(entity)
    }

    private fun onCrit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {

    }

    private fun getBloodCharge(stack: ItemStack): Float { return stack.get(ModItemComponents.BLOOD_CHARGE) ?: 0.0f }
    private fun setBloodCharge(stack: ItemStack, value: Float) { stack.set(ModItemComponents.BLOOD_CHARGE, Math.clamp(value, 0f, 1f)) }

    // SETTINGS //
    override fun isUsedOnRelease(stack: ItemStack): Boolean = false
    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean = !miner.isCreative
    override fun getMaxUseTime(stack: ItemStack?, user: LivingEntity?): Int = 72000
    override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean = ingredient.item == ModItems.LIVING_STEEL
    override fun getDefaultStack(): ItemStack {
        val stack = ItemStack(this)
        setBloodCharge(stack, 0.0f)
        return stack
    }

    // VISUALS //
    override fun getUseAction(stack: ItemStack?): UseAction = UseAction.SPEAR
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
        val percentage = if(stack.get(ModItemComponents.BLOOD_CHARGE) != null) (stack.get(ModItemComponents.BLOOD_CHARGE)!! * 100f).toInt() else 0
        if(Screen.hasShiftDown()) tooltip.add(Text.literal("Blood charge percentage: ").setStyle(EMPTY.withColor(Formatting.GRAY)).append(MialeeText.withColor(Text.literal("$percentage%").setStyle(EMPTY), color)))

        super.appendTooltip(stack, context, tooltip, type)

        if(!Screen.hasShiftDown()) tooltip.add(Text.translatable("tooltip.${CleanCut.MOD_ID}.carion_cleaver.shift"))
    }

    companion object {
        val BLOOD_UI_COLOR = "#de0d2b"

        val BLOOD_GAIN_KILL = 0.42f // How much of the blood is gained when killing an entity
        val BLOOD_GAIN_CRIT = 0.09f // How much of the blood is gained when crit an entity
        val BLOOD_GAIN_HIT = 0.01f // How much of the blood is gained when hitting an entity
        val BLOOD_ABILITY_USAGE = 1.0f // How much of the blood is used up when using an ability

        val BASE_ATTACK_DAMMAGE = 3.5f
        val BASE_ATTACK_SPEED = -2.4f

        val DEFAULT_MINING_SPEED = 1.0f
        val MINING_DAMAGE_PER_BLOCK = 2

        fun createItemSettings(): Settings = Settings()
            .attributeModifiers(ATTRIBUTE_MODIFIERS)
            .component(DataComponentTypes.TOOL, ToolComponent(listOf(), DEFAULT_MINING_SPEED, MINING_DAMAGE_PER_BLOCK))

        private val ATTRIBUTE_MODIFIERS: AttributeModifiersComponent = createAttributeModifiers()
        private fun createAttributeModifiers() = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, (BASE_ATTACK_DAMMAGE + ToolMaterials.NETHERITE.attackDamage).toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND )
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, BASE_ATTACK_SPEED.toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build()
    }

    override fun createEntity(
        world: World?,
        pos: Position?,
        stack: ItemStack?,
        direction: Direction?,
    ): ProjectileEntity {
        TODO("Not yet implemented")
    }

}