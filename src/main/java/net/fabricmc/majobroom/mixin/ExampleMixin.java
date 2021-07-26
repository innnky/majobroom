package net.fabricmc.majobroom.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "method_7555()V")
	private void init(CallbackInfo info) {
//		System.out.println("This line is printed by an majobroom mod mixin!");
	}
}
