package net.fabricmc.majobroom.armors;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class ArmorFabric implements ArmorMaterial {
    public static final int[] BASE_DURABILITY = new int[]{20,30,25,15};//定义护甲的耐久，此处数据按照从头到脚的顺序
    public static final int[] BASE_PROTECTION_AMOUNT = new int[]{2,5,6,3};//定义护甲的保护值，顺序同上

    @Override
    public int getDurability(EquipmentSlot arg0) {
        return BASE_DURABILITY[arg0.getEntitySlotId()]*25;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot arg0) {
        return BASE_PROTECTION_AMOUNT[arg0.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 100;//设置附魔等级
    }

    @Override
    public SoundEvent getEquipSound() {
        return null;//设置使用时的声音，可选
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;//设置使用铁砧修复的配方，可选
    }

    @Override
    public String getName() {
        return "majowearable";//设置名称，后期添加材质需要使用
    }

    @Override
    public float getToughness() {
        return 10;//设置武器韧性
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}