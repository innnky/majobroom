package net.fabricmc.majobroom.config;

import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.annotations.ConfigEntry;

public class MajoBroomConfig extends ConfigBase{


    @ConfigEntry(key = "maxMoveSpeed",doubleDefault = 30)
    public double maxMoveSpeed;
    @ConfigEntry(key = "requireXpLevel",booleanDefault = true)
    public boolean requireXpLevel;
    @ConfigEntry(key = "autoThirdPersonView",booleanDefault = true)
    public boolean autoThirdPersonView;

    private MajoBroomConfig() {
        super(MajoBroom.MODID);
    }

    private static MajoBroomConfig instance;
    public static MajoBroomConfig getInstance() {
        return instance == null ? (instance = new MajoBroomConfig()) : instance;
    }
}
