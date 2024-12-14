package net.dustley.clean_cut.item.cleaver

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.component.ModItemComponents
import net.dustley.clean_cut.enchantment.ModEnchantments
import net.dustley.clean_cut.entity.thrown_cleaver.ThrownCleaverEntity
import net.dustley.clean_cut.item.ModItems
import net.dustley.clean_cut.util.MialeeText
import net.minecraft.block.BlockState
import net.minecraft.client.gui.screen.Screen
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.ToolComponent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
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
import net.minecraft.world.World

open class CarrionCleaverItem : AxeItem(ToolMaterials.NETHERITE, createItemSettings())
{

    open fun isRose() = false

    override fun postDamageEntity(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        super.postDamageEntity(stack, target, attacker)

        var chargeAmount = BLOOD_GAIN_HIT
        if(target.isDead) chargeAmount += BLOOD_GAIN_KILL
        if(attacker.fallDistance >= 0.01f) chargeAmount += BLOOD_GAIN_CRIT

        setBloodCharge(stack, getBloodCharge(stack) + chargeAmount)

        if(attacker.fallDistance >= 0.01f) onCrit(stack, target, attacker)

        if(getIsActive(stack)) {
            target.setVelocity(0.0, 0.0, 0.0)
            target.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 5, 2, true, false))
        }

    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        user.setCurrentHand(hand)
        return TypedActionResult.consume(stack)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        user.playSound(SoundEvents.BLOCK_NETHERRACK_HIT)

        val charge = getBloodCharge(stack)

        if(getEnchantmentType(stack) == CleaverEnchantmentType.BLOOD_RUSH) {
            if(getBloodCharge(stack) > 0.75f) setIsActive(stack,true)
        }

//        if(charge >= BLOOD_ABILITY_USAGE) setBloodCharge(stack, charge - BLOOD_ABILITY_USAGE)
        onAbility(stack, world, user, charge)

        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        var drainageAmount = BLOOD_DRAIN_USAGE
        if (!selected) drainageAmount = BLOOD_DRAIN_USAGE * 0.5f

        if(entity is PlayerEntity && selected && !world.isClient) {
            val color = getBloodUIColor().substring(1).toInt(16) // Convert hex to int
            val percentage = if(stack.get(ModItemComponents.BLOOD_CHARGE) != null) (stack.get(ModItemComponents.BLOOD_CHARGE)!! * 100f).toInt() else 0
            val text = Text.literal("Blood: ").setStyle(EMPTY.withColor(Formatting.GRAY)).append(MialeeText.withColor(Text.literal("$percentage%").setStyle(EMPTY), color))
            entity.sendMessage(text, true)
        }

        if(getEnchantmentType(stack) == CleaverEnchantmentType.BLOOD_RUSH) {
            if(getIsActive(stack)) {
                if (entity is LivingEntity) {
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.SPEED, 5, 2, true, false))
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 5, 0, true, false))
                }
            }

            if (entity is LivingEntity && entity.isUsingRiptide) {
                val velocityDirection = entity.velocity.normalize()
                val velocityTarget = entity.rotationVector.normalize()

                entity.velocity = velocityDirection.lerp(velocityTarget, 0.2).multiply(2.5)
                drainageAmount *= 2
            }
        }

        if(getIsActive(stack)) {
            setBloodCharge(stack, getBloodCharge(stack) - drainageAmount)
            if (getBloodCharge(stack) <= 0.0001f) {
                setIsActive(stack, false)
                setBloodCharge(stack, 0f)
            }
        }
    }

    private fun onThrow(stack: ItemStack, world: World, user: LivingEntity, power:Float) {

        val entity = ThrownCleaverEntity(user, world, 2.5, (BASE_ATTACK_DAMMAGE + ToolMaterials.NETHERITE.attackDamage).toDouble(), stack.copyComponentsToNewStack(stack.item, 1), isRose())
        entity.enchantmentType = getEnchantmentType(stack)
        stack.decrement(1)
        world.spawnEntity(entity)

    }

    private fun getEnchantmentType(stack: ItemStack):CleaverEnchantmentType {
        EnchantmentHelper.getEnchantments(stack).enchantments.forEach {
            val key = it.key.get()
            when (key) {
                ModEnchantments.BLOODRUSH -> return CleaverEnchantmentType.BLOOD_RUSH
                ModEnchantments.CHARLATAN -> return CleaverEnchantmentType.CHARLATAN
                ModEnchantments.HUNTRESS -> return CleaverEnchantmentType.HUNTRESS
                ModEnchantments.NURSING -> return CleaverEnchantmentType.NURSING
            }
        }
        return CleaverEnchantmentType.NONE
    }

    private fun onAbility(stack: ItemStack, world: World, user: LivingEntity, power:Float) {
        if(getEnchantmentType(stack) == CleaverEnchantmentType.BLOOD_RUSH && getIsActive(stack)) {
            if(user is PlayerEntity) user.itemCooldownManager.set(this, 20)
            user.velocity = user.rotationVector.multiply(2.5)
            if (getIsActive(stack) && user is PlayerEntity) user.useRiptide(20, 8f, stack)
        }

        if(getEnchantmentType(stack) == CleaverEnchantmentType.HUNTRESS) {
            if (getIsActive(stack)) CleanCut.LOGGER.info("A")
        }

        if(!(getEnchantmentType(stack) == CleaverEnchantmentType.BLOOD_RUSH || getEnchantmentType(stack) == CleaverEnchantmentType.HUNTRESS)) onThrow(stack, world, user, 1f)
    }

    private fun onCrit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {

    }

    fun getBloodCharge(stack: ItemStack): Float { return stack.get(ModItemComponents.BLOOD_CHARGE) ?: 0.0f }
    fun setBloodCharge(stack: ItemStack, value: Float) { stack.set(ModItemComponents.BLOOD_CHARGE, Math.clamp(value, 0f, 1f)) }

    fun getIsActive(stack: ItemStack): Boolean { return stack.get(ModItemComponents.IS_ACTIVE) ?: false }
    fun setIsActive(stack: ItemStack, value: Boolean) { stack.set(ModItemComponents.IS_ACTIVE, value) }

    // SETTINGS //
    override fun isUsedOnRelease(stack: ItemStack): Boolean = false
    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean = !miner.isCreative
    override fun getMaxUseTime(stack: ItemStack?, user: LivingEntity?): Int = 72000
    override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean = ingredient.item == ModItems.LIVING_STEEL

    // VISUALS //
    override fun getUseAction(stack: ItemStack): UseAction = if(getEnchantmentType(stack) == CleaverEnchantmentType.BLOOD_RUSH || getEnchantmentType(stack) == CleaverEnchantmentType.HUNTRESS) UseAction.BOW else UseAction.SPEAR
    override fun isItemBarVisible(stack: ItemStack): Boolean { return getBloodCharge(stack) < BLOOD_ABILITY_USAGE || getIsActive(stack) }
    override fun getItemBarStep(stack: ItemStack): Int = Math.round(getBloodCharge(stack) * 13.0).toInt()
    override fun getItemBarColor(stack: ItemStack): Int = getBloodUIColor().substring(1).toInt(16) // Convert hex to int

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        tooltip.add(Text.translatable("tooltip.${CleanCut.MOD_ID}.${getID()}").setStyle(EMPTY.withColor(Formatting.GRAY)))

        val color = getBloodUIColor().substring(1).toInt(16) // Convert hex to int
        val percentage = if(stack.get(ModItemComponents.BLOOD_CHARGE) != null) (stack.get(ModItemComponents.BLOOD_CHARGE)!! * 100f).toInt() else 0
        if(Screen.hasShiftDown()) tooltip.add(Text.literal("Blood charge percentage: ").setStyle(EMPTY.withColor(Formatting.GRAY)).append(MialeeText.withColor(Text.literal("$percentage%").setStyle(EMPTY), color)))

        super.appendTooltip(stack, context, tooltip, type)

        if(!Screen.hasShiftDown()) tooltip.add(Text.translatable("tooltip.${CleanCut.MOD_ID}.${getID()}.shift"))
    }

    open fun getID() = "carrion_cleaver"
    open fun getBloodUIColor() = "#de0d2b"

    companion object {

        val BLOOD_GAIN_PROJECTILE = 0.2f // How much of the blood is gained when its a projectile
        val BLOOD_GAIN_KILL = 0.03f // How much of the blood is gained when killing an entity
        val BLOOD_GAIN_CRIT = 0.01f // How much of the blood is gained when crit an entity
        val BLOOD_GAIN_HIT = 0.01f // How much of the blood is gained when hitting an entity
        val BLOOD_ABILITY_USAGE = 0.25f // How much of the blood is used up when using an ability

        val BLOOD_DRAIN_USAGE = 0.005f // How much of the blood is used up when activated by bloodrush

        val BASE_ATTACK_DAMMAGE = 3.5f
        val BASE_ATTACK_SPEED = -2.4f

        val DEFAULT_MINING_SPEED = 1.0f
        val MINING_DAMAGE_PER_BLOCK = 2

        fun createItemSettings(): Settings = Settings()
            .attributeModifiers(ATTRIBUTE_MODIFIERS)
            .component(DataComponentTypes.TOOL, ToolComponent(listOf(), DEFAULT_MINING_SPEED, MINING_DAMAGE_PER_BLOCK))
            .component(ModItemComponents.BLOOD_CHARGE, 0f)
            .component(ModItemComponents.IS_ACTIVE, false)

        private val ATTRIBUTE_MODIFIERS: AttributeModifiersComponent = createAttributeModifiers()
        private fun createAttributeModifiers() = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, (BASE_ATTACK_DAMMAGE + ToolMaterials.NETHERITE.attackDamage).toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND )
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, BASE_ATTACK_SPEED.toDouble(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build()
    }

}