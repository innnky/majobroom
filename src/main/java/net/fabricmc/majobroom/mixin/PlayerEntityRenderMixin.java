package net.fabricmc.majobroom.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.client.render.feature.MajoClothFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T,M> {

    public PlayerEntityRenderMixin(EntityRendererFactory.Context ctx, M model, float shadowRadius) {
        super(ctx,model,shadowRadius);
        throw new AssertionError();
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci){
        addFeature(new MajoClothFeatureRenderer((PlayerEntityRenderer)(Object)this, new BipedEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR)), new BipedEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR : EntityModelLayers.PLAYER_OUTER_ARMOR))));
    }
}
