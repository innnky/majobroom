package net.fabricmc.majobroom.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.armors.BaseArmor;
import net.fabricmc.majobroom.armors.MajoWearableModel;
import net.fabricmc.majobroom.config.MajoBroomConfig;
import net.fabricmc.majobroom.sound.BroomFlyingSound;
import net.fabricmc.majobroom.sound.BroomFlyingSoundWrapper;
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
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BroomEntity extends BoatEntity {
    public BroomEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
        initializeSound();
    }

    private void initializeSound(){
        broomFlyingSound = new BroomFlyingSoundWrapper(this);
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
    private float floatingValue = 0;
    private float floatingCounts = 0;
    private  float prevFloatingValue = 0;

    private BroomFlyingSoundWrapper broomFlyingSound = null;

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
        passenger.setPosition(passenger.getX(),passenger.getY()+0.6 +floatingValue,passenger.getZ());
    }

    @Environment(EnvType.CLIENT)
    private void clientTick(){

        if (passenger != null){
            if ((!this.hasPassengers()) && this.world.isClient && MinecraftClient.getInstance().player.getId() == passenger.getId()){
                if(MajoBroomConfig.getInstance().autoThirdPersonView) {
                    MinecraftClient.getInstance().options.setPerspective(Perspective.FIRST_PERSON);
                }
                broomFlyingSound.stop();
                passenger = null;
            }
        }
        else{
            if(this.hasPassengers() && this.getFirstPassenger().getId()==MinecraftClient.getInstance().player.getId()){
                broomFlyingSound.play();
                if(MajoBroomConfig.getInstance().autoThirdPersonView){
                    MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }
                passenger = MinecraftClient.getInstance().player;
                if(!MajoBroomConfig.getInstance().classicalMovement) {
                    passenger.setYaw(MathHelper.wrapDegrees(this.getYaw() + 90));
                    passenger.setHeadYaw(MathHelper.wrapDegrees(this.getYaw() + 90));
                }
            }
        }

        if(!MajoBroomConfig.getInstance().classicalMovement && passenger!=null){
            float deltaYaw =passenger.getYaw()- (this.getYaw() + 90);
            if(isLogicalSideForUpdatingMovement()){
                this.setYaw(this.getYaw() + MathHelper.wrapDegrees(deltaYaw));
            }
            passenger.setYaw(MathHelper.wrapDegrees(this.getYaw() + 90));
            passenger.setHeadYaw(MathHelper.wrapDegrees(this.getYaw() + 90));
        }

    }

    @Override
    public void tick() {

        autoFall();




        if (this.world.isClient()){
            clientTick();
            updateKeys();
            if(this.hasPassengers() && MinecraftClient.getInstance().player.getId() == this.getFirstPassenger().getId()){
                broomFlyingSound.tick();
            }
            if(isLogicalSideForUpdatingMovement()){
                var current_v = this.getVelocity();
                var rotation_v = this.getRotationVector();
                rotation_v = new Vec3d(-rotation_v.z,rotation_v.y,rotation_v.x);
                float target_v = 0;
                if (passenger != null && ((PlayerEntity)passenger).getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof BaseArmor){
                    maxspeed = 0.6f;
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

            smoothMovementFromOtherPlayer();
        }
        updateFloatingValue();

    }

    private int smoothcd = 0;
    private void smoothMovementFromOtherPlayer() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.smoothcd = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }

        if (this.smoothcd > 0) {
            double d = this.getX() + (this.smoothX - this.getX()) / (double)this.smoothcd;
            double e = this.getY() + (this.smoothY - this.getY()) / (double)this.smoothcd;
            double f = this.getZ() + (this.smoothZ - this.getZ()) / (double)this.smoothcd;
            double g = MathHelper.wrapDegrees(this.smoothYaw - (double)this.getYaw());
            this.setYaw(this.getYaw() + (float)g / (float)this.smoothcd);
            this.setPitch(this.getPitch() + (float)(this.smoothPitch - (double)this.getPitch()) / (float)this.smoothcd);
            --this.smoothcd;
            this.setPosition(d, e, f);
            this.setRotation(this.getYaw(), this.getPitch());
        }
    }

    private double smoothX,smoothY,smoothZ;float smoothYaw,smoothPitch;

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        super.updateTrackedPositionAndAngles(x, y, z, yaw, pitch, interpolationSteps, interpolate);
        this.smoothX = x;
        this.smoothY = y;
        this.smoothZ = z;
        this.smoothYaw = yaw;
        this.smoothPitch = pitch;
        this.smoothcd=10;
    }



    @Override
    protected void refreshPosition() {
        super.refreshPosition();
    }

    int cd = 20;
    private void updateKeys(){
        forward = MinecraftClient.getInstance().options.forwardKey.isPressed();
        back = MinecraftClient.getInstance().options.backKey.isPressed();
        left = MinecraftClient.getInstance().options.leftKey.isPressed();
        right = MinecraftClient.getInstance().options.rightKey.isPressed();
        up = MinecraftClient.getInstance().options.jumpKey.isPressed();
        down = MinecraftClient.getInstance().options.sprintKey.isPressed();

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
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof PlayerEntity s){
            this.kill();
            if (!s.isCreative()) {
                if(!s.getInventory().insertStack(MajoBroom.broomItem.getDefaultStack())){
                    dropItem(asItem());
                }
            }
            return true;
        }
        return false;
    }

    private void updateFloatingValue(){
        this.prevFloatingValue = this.floatingValue;
        this.floatingCounts += 0.05f;
        if (this.floatingCounts > 6.28f){
            this.floatingCounts -= 6.28f;
        }
        this.floatingValue = 0.1f * MathHelper.sin(2*this.floatingCounts);
    }

    public float getFloatingValue(float tickDelta){
        return MathHelper.lerp(tickDelta, this.prevFloatingValue, this.floatingValue);
    }

    public void autoFall(){
        if (!this.hasPassengers()){
            this.setVelocity(0,-0.1,0);
            this.move(MovementType.SELF,this.getVelocity());
        }
    }


}