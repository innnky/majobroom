package net.fabricmc.majobroom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.client.itemgroup.FabricCreativeGuiComponents;
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
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.registry.Registry;


public class MajoBroom implements ModInitializer {
	public static final String MODID = "majobroom";
	private static final RegistryKey<ItemGroup> majoGroupRegistryKey = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MODID, "majo_group"));
	public static final ItemGroup majoGroup = FabricItemGroup.builder()
		.displayName(Text.translatable("itemGroup.majobroom.majo_group"))
		.icon(() -> new ItemStack(MajoBroom.broomItem)).build();

	//盔甲部分
	public static final ArmorMaterial FABRIC_ARMOR = new ArmorFabric();
	public static final Item broomItem = new BroomItem(new Item.Settings().maxCount(1));
//	group(MajoBroom.majoGroup)
	public static final Item majoCloth = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.CHEST);
//	public static final Item majoStocking = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.FEET);
	public static final Item majoHat = new BaseArmor(FABRIC_ARMOR, EquipmentSlot.HEAD);

	//ItemGroup
	static {
		ItemGroupEvents.modifyEntriesEvent(majoGroupRegistryKey).register(MajoBroom::setItemGroup);
	}

	protected static void setItemGroup(FabricItemGroupEntries entries) {
		entries.add(broomItem);
		entries.add(majoHat);
		entries.add(majoCloth);
		//entries.add(majoStocking);
	}

	//实体
	public static final EntityType<BroomEntity> BROOM_ENTITY_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MODID, "majo_broom"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BroomEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
	);

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM_GROUP, majoGroupRegistryKey, majoGroup);
		Registry.register(Registries.ITEM, new Identifier(MODID, "broom_item"), broomItem);
		Registry.register(Registries.ITEM, new Identifier(MODID, "majo_cloth"), majoCloth);
//		Registry.register(Registries.ITEM, new Identifier(MODID, "stocking"), majoStocking);
		Registry.register(Registries.ITEM, new Identifier(MODID, "majo_hat"), majoHat);
//		FabricDefaultAttributeRegistry.register(CUBE, CubeEntity);
//		Registry.register(Registries.ITEM, new Identifier(MODID, "fabric_helmet"), new BaseArmor(FABRIC_ARMOR, EquipmentSlot.HEAD));
		MajoBroomConfig.getInstance();

	}



	public static void main(String[] args) {
		System.out.println("sda");
	}
}
