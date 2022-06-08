package net.fabricmc.majobroom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.item.group.FabricCreativeGuiComponents;
import net.fabricmc.majobroom.armors.ArmorFabric;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.fabricmc.majobroom.config.MajoBroomConfig;
import net.fabricmc.majobroom.entity.BroomEntity;
import net.fabricmc.majobroom.items.BroomItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MajoBroom implements ModInitializer {
	public static final String MODID = "majobroom";
	public static final ItemGroup majoGroup = FabricItemGroupBuilder.build(new Identifier("majobroom", "majo_group"), () -> new ItemStack(MajoBroom.broomItem));

	//盔甲部分
	public static final ArmorMaterial FABRIC_ARMOR = new ArmorFabric();
	public static final Item broomItem = new BroomItem(new Item.Settings().group(MajoBroom.majoGroup).maxCount(1));
	public static final Item majoCloth = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.CHEST);
//	public static final Item majoStocking = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.FEET);
	public static final Item majoHat = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.HEAD);


	//实体
	public static final EntityType<BroomEntity> BROOM_ENTITY_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MODID, "majo_broom"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BroomEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
	);

	@Override
	public void onInitialize() {

		Registry.register(Registry.ITEM, new Identifier(MODID, "broom_item"), broomItem);
		Registry.register(Registry.ITEM, new Identifier(MODID, "majo_cloth"), majoCloth);
//		Registry.register(Registry.ITEM, new Identifier(MODID, "stocking"), majoStocking);
		Registry.register(Registry.ITEM, new Identifier(MODID, "majo_hat"), majoHat);
//		FabricDefaultAttributeRegistry.register(CUBE, CubeEntity);
//		Registry.register(Registry.ITEM, new Identifier(MODID, "fabric_helmet"), new BaseArmor(FABRIC_ARMOR, EquipmentSlot.HEAD));
		MajoBroomConfig.getInstance();


	}



	public static void main(String[] args) {
		System.out.println("sda");
	}
}
