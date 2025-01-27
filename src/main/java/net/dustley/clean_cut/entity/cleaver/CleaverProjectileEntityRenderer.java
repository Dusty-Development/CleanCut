package net.dustley.clean_cut.entity.cleaver;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.shape.VoxelShapes;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CleaverProjectileEntityRenderer extends EntityRenderer<CleaverProjectileEntity> {

    ItemRenderer itemRenderer;

    public CleaverProjectileEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(CleaverProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // HIT-BOX
        if(MinecraftClient.getInstance().gameRenderer.getClient().getDebugHud().shouldShowDebugHud()) {
            matrices.push();
            matrices.translate(0f, entity.getHeight()*0.5f, 0f);

            Vector3f color = hexToRGB(entity.getVariantFromStack().color);
            WorldRenderer.drawShapeOutline(
                    matrices,
                    vertexConsumers.getBuffer(RenderLayer.LINES),
                    VoxelShapes.cuboid(entity.getHitBox()),
                    0.0,
                    0.0,
                    0.0,
                    color.x, color.y, color.z, 1f,
                    true
            );

            matrices.pop();
        }

        // ITEM
        matrices.push();

        matrices.translate(0f, entity.getHeight()*0.5f, 0f);

        var yawQuaternion = new Quaternionf().rotateY((float) Math.toRadians(entity.getRotationAnimation(tickDelta)));
        matrices.multiply(yawQuaternion);

        var rollQuaternion = new Quaternionf().rotateX((float) Math.toRadians(90.0));
        matrices.multiply(rollQuaternion);

        var pitchQuaternion = new Quaternionf().rotateX((float) Math.toRadians(entity.RandomAngle));
        matrices.multiply(pitchQuaternion);

        itemRenderer.renderItem(
                entity.getWeaponStack(),
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override public Identifier getTexture(CleaverProjectileEntity entity) { return null; }

    public static Vector3f hexToRGB(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1); // Remove the #
        }

        // Parse the hex values
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        // Convert to float values between 0 and 1
        return new Vector3f(r / 255.0f, g / 255.0f, b / 255.0f);
    }

}