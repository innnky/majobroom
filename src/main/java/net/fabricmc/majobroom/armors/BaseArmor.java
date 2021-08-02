package net.fabricmc.majobroom.armors;

import net.fabricmc.majobroom.MajoBroom;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

public class BaseArmor extends DyeableArmorItem implements DyeableItem {

    public BaseArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(MajoBroom.majoGroup));
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("color", 99) ? nbtCompound.getInt("color") : 14525383;
    }

//    @Override
//    public boolean hasGlint(ItemStack stack) {
//        return false;
//    }
}