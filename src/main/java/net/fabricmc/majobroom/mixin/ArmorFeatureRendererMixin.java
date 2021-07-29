package net.fabricmc.majobroom.mixin;

import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.fabricmc.majobroom.armors.MajoWearableModel;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    @Shadow
    protected abstract boolean usesSecondLayer(EquipmentSlot slot);

    @Shadow
    @Final
    private A leggingsModel;

    @Shadow
    @Final
    private A bodyModel;


    @Shadow protected abstract void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model);

    @Shadow protected abstract A getArmor(EquipmentSlot slot);

    @Shadow public abstract void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l);

    //    @Inject(at = @At("HEAD"), method = "getArmor", cancellable = true)
//    private void getArmor(EquipmentSlot slot, CallbackInfoReturnable<A> cir) {
//        System.out.println("before return");
//        cir.setReturnValue(this.usesSecondLayer(slot) ? this.leggingsModel : this.bodyModel);
//    }
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {

        if (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof BaseArmor) {
            A model = (A) new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_hat.json");
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, model);
        }else {
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, this.getArmor(EquipmentSlot.HEAD));
        }

        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BaseArmor){
            A model = (A) new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_cloth.json");
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, i, model);
        }else {
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, i, this.getArmor(EquipmentSlot.CHEST));
        }

        this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.LEGS, i, this.getArmor(EquipmentSlot.LEGS));
        this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.FEET, i, this.getArmor(EquipmentSlot.FEET));
        ci.cancel();

    }
    @Inject(at = @At("HEAD"), method = "getArmorTexture", cancellable = true)
    private void getArmorTexture(ArmorItem item, boolean legs, String overlay, CallbackInfoReturnable<Identifier> cir) {
        if (item instanceof BaseArmor){
            if (item.toString().equals("majo_hat")){
                cir.setReturnValue(new Identifier(MajoBroom.MODID,"jsonmodels/textures/"+item.toString()+(overlay == null ? "" : "_" + overlay) + ".png"));
            }else {
                cir.setReturnValue(new Identifier(MajoBroom.MODID,"jsonmodels/textures/"+item.toString() + ".png"));
            }
        }

    }
}