package net.fabricmc.majobroom.entity;

import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.fabricmc.majobroom.armors.MajoWearableModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BroomEntity extends BoatEntity {
    public BroomEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }
    private boolean forward = false;
    private boolean back = false;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private float yaw_v = 0;
    private float g_v = 0;
    private Entity passenger;
    private final float accspeed = 0.2f;
    private  float maxspeed = 1f;
    private final float max_gspeed = 0.5f;
    @Override
    public Item asItem() {
        return MajoBroom.broomItem;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {

        return this.getPassengerList().size() < 1;
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        super.updatePassengerPosition(passenger);
        passenger.setPosition(passenger.getX(),passenger.getY()+0.6,passenger.getZ());
    }

    @Override
    public void tick() {
//        super.tick();

        if (this.hasPassengers()){
            if (passenger == null){
                if (this.world.isClient && MinecraftClient.getInstance().player.getId() == this.getFirstPassenger().getId()){
                    MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }
            }
            passenger = this.getFirstPassenger();
        }else {
            if (passenger != null){
                if (this.world.isClient && MinecraftClient.getInstance().player.getId() == passenger.getId()){
                    MinecraftClient.getInstance().options.setPerspective(Perspective.FIRST_PERSON);
                }
            }
            passenger = null;
        }

        if (this.world.isClient()){
            updateKeys();
            if (isLogicalSideForUpdatingMovement()) {
                var current_v = this.getVelocity();
                var rotation_v = this.getRotationVector();
                rotation_v = new Vec3d(-rotation_v.z,rotation_v.y,rotation_v.x);
                float target_v = 0;
                if (passenger != null && ((PlayerEntity)passenger).getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof BaseArmor){
                    maxspeed = 1;
                }else {
                    maxspeed = 0.3f;
                }
                if (forward) {
                    target_v = maxspeed;
                } else if (back) {
                    target_v = -maxspeed;
                }else {
                    target_v = 0;
                }
                var targetv3d = rotation_v.multiply(target_v);
                var new_v3d = current_v.multiply(1-accspeed).add(targetv3d.multiply(accspeed));

                float target_g = 0;
                if (up){
                    target_g += max_gspeed;
                }
                if (down){
                    target_g -= max_gspeed;
                }
                g_v = (float) (g_v*(1-accspeed) +target_g*accspeed);

                float target_yaw = 0;
                if (left){
                    target_yaw-=10;
                }
                if (right){
                    target_yaw+=10;
                }
                yaw_v = (float) (yaw_v*(1-accspeed)+target_yaw*accspeed);

                this.setYaw(this.getYaw()+yaw_v);
                if (this.getFirstPassenger() !=null){
                    Entity passenger = this.getFirstPassenger();
                    passenger.setYaw(passenger.getYaw()+yaw_v);
                }
                this.setVelocity(new_v3d.x,g_v,new_v3d.z);

                this.move(MovementType.SELF, this.getVelocity());
            }
        }
    }

    private void updateKeys(){
        forward = MinecraftClient.getInstance().options.keyForward.isPressed();
        back = MinecraftClient.getInstance().options.keyBack.isPressed();
        left = MinecraftClient.getInstance().options.keyLeft.isPressed();
        right = MinecraftClient.getInstance().options.keyRight.isPressed();
        up = MinecraftClient.getInstance().options.keyJump.isPressed();
        down = MinecraftClient.getInstance().options.keySprint.isPressed();

    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else {
            if (!this.world.isClient) {
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        }
    }

    @Override
    public void dismountVehicle() {
//        System.out.println("dwdwdwdwdw");
        super.dismountVehicle();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
//        return super.damage(source, amount);
        if (source.getSource() instanceof PlayerEntity s){
            this.kill();
            if (!s.isCreative()) {
                s.getInventory().insertStack(MajoBroom.broomItem.getDefaultStack());
            }
            return true;
        }
        return false;
    }
}