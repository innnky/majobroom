package net.fabricmc.majobroom.items;

import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.entity.BroomEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BroomItem extends Item {
    public BroomItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        var result = raycast(world,user, RaycastContext.FluidHandling.ANY);
        if (result.getType() == HitResult.Type.BLOCK){
            BlockPos blockPos = result.getBlockPos();
            if (!world.isClient()){
                BroomEntity broomEntity = MajoBroom.BROOM_ENTITY_ENTITY_TYPE.create(world);
                broomEntity.setPosition(blockPos.getX()+0.5,blockPos.getY()+1.5,blockPos.getZ()+0.5);
                world.spawnEntity(broomEntity);
                itemStack.decrement(1);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return super.getItemBarStep(stack);
    }



}
