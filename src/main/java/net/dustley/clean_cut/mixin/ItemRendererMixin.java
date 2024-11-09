package net.dustley.clean_cut.mixin;

import net.dustley.clean_cut.client.ClientSetup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "LOAD", ordinal = 0), ordinal = 0, argsOnly = true)
    public BakedModel render(BakedModel bakedModel, ItemStack stack, ModelTransformationMode itemDisplayContext) {
        Identifier itemKey = Registries.ITEM.getId(stack.getItem());
        if (ClientSetup.LARGE_MODEL.containsKey(new ModelIdentifier(itemKey, "inventory")) && ClientSetup.LARGE_MODEL.get(new ModelIdentifier(itemKey, "inventory")).containsKey(itemDisplayContext)) {
            BakedModel replacedModel = ClientSetup.BAKED_MODELS.get(ClientSetup.getCustomModel(new ModelIdentifier(itemKey, "inventory"), itemDisplayContext));
            if (replacedModel != null) {
                return replacedModel.getOverrides().apply(replacedModel, stack, MinecraftClient.getInstance().world, null, 0);
            }
        }
        return bakedModel;
    }
}
