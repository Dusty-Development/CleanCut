package net.dustley.clean_cut.component;

import com.mojang.serialization.Codec;
import net.dustley.clean_cut.CleanCut;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModItemComponents {

    public static final FoodComponent LIVING_STEEL_FOOD = new FoodComponent.Builder()
            .nutrition(10)
            .saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 5), 0.25f)
            .build();

    public static final ComponentType<String> CLEAVER_VARIANT = register("cleaver_variant", builder -> builder.codec(Codec.STRING));
    public static final ComponentType<Float> BLOOD_CHARGE = register("blood_charge", builder -> builder.codec(Codec.FLOAT));
    public static final ComponentType<Boolean> IS_ACTIVE = register("is_active", builder -> builder.codec(Codec.BOOL));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                CleanCut.identifier(name),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }

    public static void registerDataComponentTypes() {
        CleanCut.LOGGER.info("Registering Data Component Types for " + CleanCut.MOD_ID);
    }
}