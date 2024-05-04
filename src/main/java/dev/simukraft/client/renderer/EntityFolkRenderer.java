package dev.simukraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import dev.simukraft.client.models.EntityFolkModel;
import dev.simukraft.entities.EntityFolk;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
    public void render(EntityFolk pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.renderNameTag(pEntity, Component.literal("TEST TEST"), pMatrixStack, pBuffer, pPackedLight);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected void renderNameTag(EntityFolk folk, Component name, PoseStack stack, MultiBufferSource buffer, int packedLight) {

        double distanceToSqr = this.entityRenderDispatcher.distanceToSqr(folk);
        float baseOffset = folk.getBbHeight() + 0.5F;
        float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);

        if (distanceToSqr > 100.0) return;

        if (this.entityRenderDispatcher.crosshairPickEntity == null) {
            renderLine(Component.literal("John Doe").withStyle(ChatFormatting.YELLOW), -0.035F, stack, -1,baseOffset + .25F, opacity, buffer, packedLight);
        } else if (this.entityRenderDispatcher.crosshairPickEntity.distanceToSqr(folk) <= 4.0) {
            // Detai view, maybe? (the original mod had less entity info for the nameplate, and I think this looks better anyhow...
            renderLine(Component.literal("John Doe").withStyle(ChatFormatting.YELLOW), -0.035F, stack, -1,baseOffset + .25F, opacity, buffer, packedLight);
        }
    }

    private void renderLine(Component text, PoseStack stack, int color, float offset, float opacity, MultiBufferSource buffer, int packedLight) {
        renderLine(text, -0.025F, stack, color, offset, opacity, buffer, packedLight);
    }

    /**
     * Render a line above the entity, scale allows you to change sizes
     *
     * @param text        {@link Component} text to render
     * @param scale       float Scale to use(vanilla -0.025F)
     * @param stack       {@link PoseStack} stack
     * @param offset      float offset, defaults to normal
     * @param opacity     float opacity ot use(vanilla)
     * @param buffer      {@link MultiBufferSource} buffer source
     * @param packedLight int packed light(idk lol)
     */
    private void renderLine(Component text, float scale, PoseStack stack, int color, float offset, float opacity, MultiBufferSource buffer, int packedLight) {
        float width = (float) (-this.getFont().width(text) / 2);
        int opacityOut = (int) (opacity * 255.0F) << 24;
        stack.pushPose();
        stack.translate(0.0F, offset, 0.0F);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.scale(scale, scale, scale);
        Matrix4f matrix4f = stack.last().pose();
        this.getFont().drawInBatch(text, width, 0, color, false, matrix4f, buffer, false, opacityOut, packedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFolk pEntity) {
        return DEFAULT_LOCATION;
    }
}
