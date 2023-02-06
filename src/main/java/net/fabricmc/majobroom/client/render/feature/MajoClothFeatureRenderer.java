package net.fabricmc.majobroom.client.render.feature;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.MajoBroom;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.fabricmc.majobroom.armors.MajoWearableModel;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class MajoClothFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends ArmorFeatureRenderer<T, M,A> {
    private static MajoWearableModel hat = null;
    private static MajoWearableModel cloth = null;
    private static MajoWearableModel foot = null;
    public MajoClothFeatureRenderer(FeatureRendererContext<T, M> context, A leggingsModel, A bodyModel) {
        super(context, leggingsModel, bodyModel);
    }
    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof BaseArmor) {
            if (hat == null){
                hat = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_hat.json", MajoWearableModel.ModelType.HEAD);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, light, (A) hat);
        }
        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BaseArmor){
            if (cloth == null){
                cloth = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(256, 256), "majo_cloth.json", MajoWearableModel.ModelType.UPPER_BODY);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, light, (A) cloth);
        }

        if (livingEntity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof BaseArmor){
            if (foot == null){
                foot = new MajoWearableModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).getRoot().createPart(64, 64), "stocking.json", MajoWearableModel.ModelType.FOOT);
            }
            this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.FEET, light, (A) foot);
        }
    }

    private boolean usesSecondLayer(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if (itemStack.getItem() instanceof BaseArmor) {
            ArmorItem armorItem = (ArmorItem)itemStack.getItem();
            if (armorItem.getSlotType() == armorSlot) {
                this.getContextModel().copyBipedStateTo(model);
                //this.getContextModel().setAttributes(model);
                this.setVisible(model, armorSlot);
                boolean bl = this.usesSecondLayer(armorSlot);
                int i = ((DyeableArmorItem)armorItem).getColor(itemStack);
                float f = (float)(i >> 16 & 255) / 255.0F;
                float g = (float)(i >> 8 & 255) / 255.0F;
                float h = (float)(i & 255) / 255.0F;
                this.renderArmorParts(matrices, vertexConsumers, light, armorItem, false, model, bl, f, g, h, (String)null);
                this.renderArmorParts(matrices, vertexConsumers, light, armorItem, false, model, bl, 1.0F, 1.0F, 1.0F, "overlay");
            }
        }
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, @Nullable String overlay) {
        //RenderLayer layer = RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, legs, overlay));
        RenderLayer layer = RenderLayer.getEntityTranslucent(this.getArmorTexture(item, legs, overlay));

        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers,layer , false, usesSecondLayer);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
    }
    private Identifier getArmorTexture(ArmorItem item, boolean legs, String overlay) {
        if (item instanceof BaseArmor){
            switch (item.toString()){
                case "majo_hat":
                    return (new Identifier(MajoBroom.MODID,"jsonmodels/textures/"+item.toString()+(overlay == null ? "" : "_" + overlay) + ".png"));
                case "majo_cloth":
                case "stocking":
                    return (new Identifier(MajoBroom.MODID,"jsonmodels/textures/"+item.toString() + ".png"));
            }
        }
        return new Identifier(MajoBroom.MODID,"empty.png");
    }
}

class MajoStockRenderLayer{

}
