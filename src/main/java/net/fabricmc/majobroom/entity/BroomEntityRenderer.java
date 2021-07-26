package net.fabricmc.majobroom.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class BroomEntityRenderer extends EntityRenderer<BroomEntity> {
    private final BroomModel broomModel = new BroomModel();
    public BroomEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(BroomEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(broomModel.getLayer(getTexture(entity)));
        float y = entity.getYaw(tickDelta);
        float p = entity.getPitch(tickDelta);
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(p));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90-y));
        broomModel.render(matrices,vertexConsumer,light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BroomEntity entity) {
        return new Identifier("majobroom", "textures/entity/broom.png");
    }
}