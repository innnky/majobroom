package net.fabricmc.majobroom.armors;

import net.fabricmc.majobroom.MajoBroom;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

public class BaseArmor extends DyeableArmorItem implements DyeableItem {

    public BaseArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, getArmorType(slot), new Item.Settings());
        DispenserBlock.registerBehavior(this,ArmorItem.DISPENSER_BEHAVIOR);//发射器穿装备
    }

    private static ArmorItem.Type getArmorType(EquipmentSlot slot) {
        switch (slot) {
            case HEAD:
                return ArmorItem.Type.HELMET;
            case CHEST:
                return ArmorItem.Type.CHESTPLATE;
            case LEGS:
                return ArmorItem.Type.LEGGINGS;
            case FEET:
                return ArmorItem.Type.BOOTS;
            default:
                throw new IllegalArgumentException("Invalid equipment slot: " + slot);
        }
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("color", 99) ? nbtCompound.getInt("color") : 14525383;
    }

}