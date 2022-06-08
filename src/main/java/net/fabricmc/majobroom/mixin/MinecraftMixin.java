package net.fabricmc.majobroom.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftMixin {
	@Shadow @Final private ItemColors itemColors;

	@Inject(at = @At("HEAD"), method = "initializeSearchProviders")
	private void initializeSearchableContainers(CallbackInfo info) {
		this.itemColors.register((stack, tintIndex) -> {
			return tintIndex > 0 ? -1 : ((BaseArmor)stack.getItem()).getColor(stack);
		}, MajoBroom.majoHat);
	}
}
