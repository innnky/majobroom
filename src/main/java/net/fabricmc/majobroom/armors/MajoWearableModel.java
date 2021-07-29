package net.fabricmc.majobroom.armors;

import com.google.common.collect.ImmutableList;
import net.fabricmc.majobroom.jsonbean.GeomtryBean;
import net.fabricmc.majobroom.utils.ModelJsonReader;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class MajoWearableModel extends BipedEntityModel<LivingEntity> {
    private final ModelPart base;
    private final Map<String, ModelPartData> bones = new HashMap();
    private final HashMap<String, GeomtryBean.BonesBean> bonesBean = new HashMap();
    public  String name ;
    public MajoWearableModel(ModelPart root, String name) {
        super(root);
        this.base = getTexturedModelData(name).createModel();
        this.name = name;
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
//        ImmutableList.of(this.base).forEach((modelRenderer) -> {
//            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
//
//        });
        if(this.bones.get("bigBody") == null){
            this.base.copyTransform(this.head);
        }else {
            this.base.getChild("bigBody").copyTransform(this.body);
            this.base.getChild("bone81").getChild("left").copyTransform(this.rightArm);
            this.base.getChild("bone81").getChild("right").copyTransform(this.leftArm);
            ModelPart dress = (ModelPart)this.base.getChild("dress");
//            dress.pivotX =dress.pivotX+0.1f;
//            dress.pivotX
//            System.out.println(111);
//            this.base.getChild()
        }
        this.base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public TexturedModelData getTexturedModelData(String path) {
        GeomtryBean model =  ModelJsonReader.readJson("jsonmodels/"+path);


        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
//        modelPartData.addChild("sdasdsads", ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 24F, 24F, 24F), ModelTransform.pivot(0F, 100F, 5F));
//
//        var another_cube = ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F);
//        modelPartData.getChild("sdasdsads").addChild("aaa",another_cube,ModelTransform.pivot(0F, 1F, 1F));


        if (model != null) {

            for (GeomtryBean.BonesBean bone : model.getBones()) {
                ModelPartBuilder newBone = ModelPartBuilder.create();
                float x=0,y=0,z=0;
                if (bone.getRotation() != null) {
                    x = 0.017453F * (Float) bone.getRotation().get(0);
                    y = 0.017453F * (Float) bone.getRotation().get(1);
                    z = 0.017453F * (Float) bone.getRotation().get(2);
                }

                if (bone.getCubes() != null) {
                    for (GeomtryBean.BonesBean.CubesBean cube : bone.getCubes()) {
                        newBone.uv((Integer) cube.getUv().get(0), (Integer) cube.getUv().get(1)).cuboid(this.convertOrigin(bone, cube, 0), this.convertOrigin(bone, cube, 1), this.convertOrigin(bone, cube, 2), (Float) cube.getSize().get(0), (Float) cube.getSize().get(1), (Float) cube.getSize().get(2));
                    }
                }

                ModelPartData nnnn = null;

                if (bone.getParent() != null) {
                    GeomtryBean.BonesBean parent = (GeomtryBean.BonesBean) this.bonesBean.get(bone.getParent());
                    ((ModelPartData) this.bones.get(bone.getParent())).addChild(bone.getName(),newBone,ModelTransform.of((Float) bone.getPivot().get(0) - (Float) parent.getPivot().get(0), (Float) parent.getPivot().get(1) - (Float) bone.getPivot().get(1), (Float) bone.getPivot().get(2) - (Float) parent.getPivot().get(2),x,y,z));
                    nnnn = ((ModelPartData) this.bones.get(bone.getParent())).getChild(bone.getName());
                } else {
                    modelPartData.addChild(bone.getName(),newBone,ModelTransform.of((Float) bone.getPivot().get(0), 24.0F - (Float) bone.getPivot().get(1), (Float) bone.getPivot().get(2),x,y,z));
                    nnnn = modelPartData.getChild(bone.getName());
                }

                this.bones.put(bone.getName(), nnnn);
                this.bonesBean.put(bone.getName(), bone);
            }
        }

        return TexturedModelData.of(modelData, model.getTexturewidth(), model.getTextureheight());
    }


    private float convertOrigin(GeomtryBean.BonesBean bones, GeomtryBean.BonesBean.CubesBean cubes, int index) {
        return index == 1 ? (Float)bones.getPivot().get(index) - (Float)cubes.getOrigin().get(index) - (Float)cubes.getSize().get(index) : (Float)cubes.getOrigin().get(index) - (Float)bones.getPivot().get(index);
    }


}