package net.fabricmc.majobroom.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.majobroom.entity.BroomEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

public class BroomFlyingSoundWrapper {
    private BroomEntity attachedInstance;

    public BroomFlyingSoundWrapper(BroomEntity attachedInstance) {
        this.attachedInstance = attachedInstance;
    }

    public void stop() {
         if(attachedInstance.world.isClient){
             stop0();
         }
    }

    public void play() {
        if(attachedInstance.world.isClient){
            play0();
        }
    }

    public void tick() {
        if(attachedInstance.world.isClient){
            tick0();
        }
    }


    @Environment(EnvType.CLIENT)
    private BroomFlyingSound bsf;
    @Environment(EnvType.CLIENT)
    private void play0(){
        if(bsf==null){
            bsf = new BroomFlyingSound(attachedInstance);
        }
        MinecraftClient.getInstance().getSoundManager().play(bsf);
    }


    @Environment(EnvType.CLIENT)
    private void stop0(){
        MinecraftClient.getInstance().getSoundManager().stop(bsf);
        bsf=null;
    }


    @Environment(EnvType.CLIENT)
    private void tick0(){
        if(bsf!=null){
            bsf.tick();
        }
    }
}

