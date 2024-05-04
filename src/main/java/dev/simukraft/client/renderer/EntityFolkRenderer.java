package dev.simukraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import dev.simukraft.client.models.EntityFolkModel;
import dev.simukraft.entities.EntityFolk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EntityFolkRenderer extends HumanoidMobRenderer<EntityFolk, EntityFolkModel> {

    private static final ResourceLocation DEFAULT_LOCATION = new ResourceLocation("textures/entity/steve.png");

    public EntityFolkRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new EntityFolkModel(pContext.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    protected void renderNameTag(EntityFolk pEntity, Component pDisplayName, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(pEntity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(pEntity, d0)) {
            boolean flag = !pEntity.isDiscrete();
            float f = pEntity.getBbHeight() + 0.5F;
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0D, f, 0.0D);
            pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            Font font = this.getFont();
            float f2 = (float) (-font.width(pDisplayName) / 2);
            font.drawInBatch(pDisplayName, f2, 0, 553648127, false, matrix4f, pBuffer, flag, j, pPackedLight);
            if (flag) {
                font.drawInBatch(pDisplayName, f2, 0, -1, false, matrix4f, pBuffer, false, 0, pPackedLight);
            }

            pMatrixStack.popPose();
        }
    }

//    private void renderLine(Component text, PoseStack pose, float offset, float opacity, Font font, MultiBufferSource buf, int packedLightCoords) {
//        float width = (float) (-font.width(text) / 2);
//        int opacityOut = (int) (opacity * 255.0F) << 24;
//        pose.pushPose();
//        pose.translate(0.0F, offset, 0.0F);
//        pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
//        pose.scale(-0.025F, -0.025F, 0.025F);
//        Matrix4f matrix4f = pose.last().pose();
//        font.drawInBatch(pDisplayName, f2, (float)i, 553648127, false, matrix4f, pBuffer, flag, j, pPackedLight);
//        pose.popPose();
//    }

    @Override
    public ResourceLocation getTextureLocation(EntityFolk pEntity) {
        return DEFAULT_LOCATION;
    }
}
