package net.dustley.clean_cut.item.weapon.cleaver;

import net.dustley.clean_cut.CleanCut;
import net.dustley.clean_cut.component.ModItemComponents;
import net.dustley.clean_cut.enchantment.ModEnchantments;
import net.dustley.clean_cut.enchantment.cleaver_modules.BloodrushEnchantmentModule;
import net.dustley.clean_cut.enchantment.cleaver_modules.EmptyEnchantmentModule;
import net.dustley.clean_cut.enchantment.cleaver_modules.ICleaverEnchantmentModule;
import net.dustley.clean_cut.enchantment.cleaver_modules.NursingEnchantmentModule;
import net.dustley.clean_cut.item.ModItems;
import net.dustley.clean_cut.util.MialeeText;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class CleaverItem extends AxeItem {

    // Simple Constructor //
    public CleaverItem() { super(ToolMaterials.NETHERITE, createItemSettings()); }

    public static float BLOOD_CHARGE_MULTIPLIER = 0.01f;

    // Module Handling //
    // Gets the enchantmentModule
    public static ICleaverEnchantmentModule getCurrentModule(ItemStack stack) {
        var enchantmentList = EnchantmentHelper.getEnchantments(stack).getEnchantments().stream().toList();
        if(!enchantmentList.isEmpty()){
            var enchantment = enchantmentList.getFirst();
            if (enchantment.hasKeyAndValue()) {
                RegistryKey<Enchantment> firstEnchantment = enchantment.getKey().get();

                if (firstEnchantment == ModEnchantments.BLOODRUSH) return BloodrushEnchantmentModule.getInstance();
                else if (firstEnchantment == ModEnchantments.NURSING) return NursingEnchantmentModule.getInstance();
            }
        }
        return EmptyEnchantmentModule.getInstance();
    }

    // Item hooks //

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean shouldHit = super.postHit(stack, target, attacker);

        float chargeAmount = 0.01f;
        if(target.isDead()) chargeAmount += 0.03f;
        if(attacker.fallDistance >= 0.01f) chargeAmount += 0.01f;

        setBloodCharge(stack, getBloodCharge(stack) + chargeAmount);

        getCurrentModule(stack).onEntityHit(stack, target, attacker);
        return shouldHit;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var event = getCurrentModule(user.getStackInHand(hand)).onUseStart(world, user, hand);
        user.setCurrentHand(hand);

        return event.orElseGet(() -> super.use(world, user, hand));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        user.playSound(SoundEvents.BLOCK_NETHERRACK_HIT);
        getCurrentModule(stack).onUseStop(stack, world, user, remainingUseTicks);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        float drainageAmount = 0.005f;
        if (!selected) drainageAmount *= 0.5f;

        if (entity instanceof PlayerEntity player && selected && !world.isClient && getCurrentModule(stack).usesBloodCharge(stack)) {
            int color = Integer.parseInt(getVariant(stack).color.substring(1), 16); // Convert hex to int
            int percentage = (stack.get(ModItemComponents.BLOOD_CHARGE) != null) ? (int) (stack.get(ModItemComponents.BLOOD_CHARGE) * 100f) : 0;

            Text text = Text.literal("Blood: ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append(MialeeText.withColor( Text.literal(percentage + "%").setStyle(Style.EMPTY), color ));
            player.sendMessage(text, true);
        }

        drainageAmount *= getCurrentModule(stack).inventoryTick(stack, world, entity, slot, selected);

        if (getIsActive(stack)) {
            setBloodCharge(stack, getBloodCharge(stack) - drainageAmount);
            if (getBloodCharge(stack) <= 0.0001f) {
                setIsActive(stack, false);
                setBloodCharge(stack, 0f);
            }
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        var event = getCurrentModule(stack).useOnEntity(stack, user, entity, hand);
        return event.orElseGet(() -> super.useOnEntity(stack, user, entity, hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var event = getCurrentModule(context.getStack()).useOnBlock(context);
        return event.orElseGet(() -> super.useOnBlock(context));
    }

    // Data //
    public static CleaverVariant getVariant(ItemStack stack) { return CleaverVariantHelper.getVariantFromString(stack.getOrDefault(ModItemComponents.CLEAVER_VARIANT, CleaverVariantHelper.CARRION.id)); }
    public static void setVariant(ItemStack stack, CleaverVariant value) { stack.set(ModItemComponents.CLEAVER_VARIANT, value.id); }


    public static float getBloodCharge(ItemStack stack) { return (stack.get(ModItemComponents.BLOOD_CHARGE) != null) ? stack.get(ModItemComponents.BLOOD_CHARGE) : 0.0f; }
    public static void setBloodCharge(ItemStack stack, float value) { stack.set(ModItemComponents.BLOOD_CHARGE, MathHelper.clamp(value, 0f, 1f)); }

    public static boolean getIsActive(ItemStack stack) { return stack.get(ModItemComponents.IS_ACTIVE) != null ? stack.get(ModItemComponents.IS_ACTIVE) : false; }
    public static void setIsActive(ItemStack stack, boolean value) { stack.set(ModItemComponents.IS_ACTIVE, value); }

    // Item Settings //
    // Only allow 1 enchantment in the case enhancement isn't installed and doing it for us
    @Override public boolean isEnchantable(ItemStack stack) { return EnchantmentHelper.getEnchantments(stack).isEmpty(); }
    @Override public int getEnchantability() { return 1; }

    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(ModItems.LIVING_STEEL); }
    @Override public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) { return !miner.isCreative(); }

    // Usage settings
    @Override public boolean isUsedOnRelease(ItemStack stack) { return false; }
    @Override public UseAction getUseAction(ItemStack stack) { return getCurrentModule(stack).getUseAction(stack); }
    @Override public int getMaxUseTime(ItemStack stack, LivingEntity user) { return 72000; }

    // Visual //
    @Override public boolean isItemBarVisible(ItemStack stack) { return (getBloodCharge(stack) < 0.99f || getIsActive(stack)) && getCurrentModule(stack).usesBloodCharge(stack); }
    @Override public int getItemBarStep(ItemStack stack) { return Math.round(getBloodCharge(stack) * 13.0f); }
    @Override public int getItemBarColor(ItemStack stack) { return Integer.parseInt(getVariant(stack).color.substring(1), 16); }

    @Override public Text getName() { return MialeeText.withColor(Text.translatable("item." + CleanCut.MOD_ID + "." + CleaverVariantHelper.CARRION.id + "_cleaver"), Integer.parseInt(CleaverVariantHelper.CARRION.color.substring(1), 16)); }
    @Override public Text getName(ItemStack stack) { return MialeeText.withColor(Text.translatable("item." + CleanCut.MOD_ID + "." + getVariant(stack).id + "_cleaver"), Integer.parseInt(getVariant(stack).color.substring(1), 16)); }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip." + CleanCut.MOD_ID + "." + getVariant(stack).id + "_cleaver").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        int color = Integer.parseInt(getVariant(stack).color.substring(1), 16); // Convert hex to int
        int percentage = stack.get(ModItemComponents.BLOOD_CHARGE) != null ? (int) (stack.get(ModItemComponents.BLOOD_CHARGE) * 100f) : 0;

        if (Screen.hasShiftDown() && getCurrentModule(stack).usesBloodCharge(stack)) {
            tooltip.add( Text.literal("Blood charge percentage: ")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY))
                .append(MialeeText.withColor( Text.literal(percentage + "%").setStyle(Style.EMPTY), color ))
            );
        }

        super.appendTooltip(stack, context, tooltip, type);
        if (!Screen.hasShiftDown() && getCurrentModule(stack).usesBloodCharge(stack)) tooltip.add(Text.translatable("tooltip." + CleanCut.MOD_ID + "." + getVariant(stack).id + "_cleaver.shift"));
    }


    // Create the item settings
    public static Settings createItemSettings() {
        return new Settings()
                .attributeModifiers(createAttributeModifiers())
                .component(DataComponentTypes.TOOL, new ToolComponent(List.of(), 1f, 2))
                .component(ModItemComponents.BLOOD_CHARGE, 0f)
                .component(ModItemComponents.CLEAVER_VARIANT, CleaverVariantHelper.CARRION.id)
                .component(ModItemComponents.IS_ACTIVE, false)
                .maxDamage(2048)
                .maxCount(1);
    }

    // Create the attribute modifiers for the item settings
    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                        BASE_ATTACK_DAMAGE_MODIFIER_ID,
                        (3.5 + ToolMaterials.NETHERITE.getAttackDamage()),
                        EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND )
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                                BASE_ATTACK_SPEED_MODIFIER_ID,
                                -2.4,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }
}
