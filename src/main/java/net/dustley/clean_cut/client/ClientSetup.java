package net.dustley.clean_cut.client;

import net.dustley.clean_cut.CleanCut;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientSetup {
    public static final Map<ModelIdentifier, Map<ModelTransformationMode, ModelIdentifier>> LARGE_MODEL = new HashMap<>();

    public static final Map<ModelIdentifier, BakedModel> BAKED_MODELS = new HashMap<>();

    public static ModelIdentifier getCustomModel(ModelIdentifier item, ModelTransformationMode context) {
        return LARGE_MODEL.containsKey(item) && LARGE_MODEL.get(item).containsKey(context) ? new ModelIdentifier(LARGE_MODEL.get(item).get(context).id().withPrefixedPath("item/"), "fabric_resource") : item;
    }

    public static void modifyBakingResult(Map<ModelIdentifier, BakedModel> models) {
        for (ModelIdentifier id : models.keySet()) {
            BAKED_MODELS.put(id, models.get(id));
            System.out.println(id);
        }

    }
    
    private static void registerSimpleSpecialModel(String id) {
        LARGE_MODEL.put(ModelIdentifier.ofInventoryVariant(CleanCut.identifier(id)), Map.of(
                ModelTransformationMode.HEAD, ModelIdentifier.ofInventoryVariant(CleanCut.identifier(id + "_inventory")),
                ModelTransformationMode.GUI, ModelIdentifier.ofInventoryVariant(CleanCut.identifier(id + "_inventory")),
                ModelTransformationMode.GROUND, ModelIdentifier.ofInventoryVariant(CleanCut.identifier(id + "_inventory"))
        ));
    }
    
    public static void registerExtraBakedModels(Consumer<ModelIdentifier> registration) {
        System.out.println("registered models");
        registration.accept(ModelIdentifier.ofInventoryVariant(CleanCut.identifier("cleaver_inventory")));
        //registration.accept(ModelIdentifier.ofInventoryVariant(CleanCut.INSTANCE.id("carrion_cleaver_overlay")));
    }

    public static void clientSetup() {
        registerSimpleSpecialModel("cleaver");
        System.out.println("client was called");
    }

}