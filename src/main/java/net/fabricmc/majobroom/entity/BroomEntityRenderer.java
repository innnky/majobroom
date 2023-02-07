package net.fabricmc.majobroom.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BroomEntityRenderer extends EntityRenderer<BroomEntity> {
    private final BroomModel broomModel = new BroomModel();
    public BroomEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(BroomEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        RenderLayer renderlayer =broomModel.getLayer(getTexture(entity));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderlayer);
        float y = entity.getYaw(tickDelta);
        float p = entity.getPitch(tickDelta);
        float floating_value = entity.getFloatingValue(tickDelta);
        matrices.push();
        matrices.translate(0,floating_value,0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(p));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90-y));
        broomModel.render(matrices,vertexConsumer,light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private final Identifier broomTexture = new Identifier("majobroom", "textures/entity/broom.png");
    private final Identifier broomTextureTransparent = new Identifier("majobroom", "textures/entity/broom_transparent.png");
    @Override
    public Identifier getTexture(BroomEntity entity) {
        if(entity.hasPassengers() && entity.getFirstPassenger().getId()== MinecraftClient.getInstance().player.getId()){
            if(MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON){

                return broomTextureTransparent;
            }
        }
        return broomTexture;
    }
}