package net.fabricmc.majobroom.config;

import net.fabricmc.majobroom.annotations.ConfigEntry;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class ConfigBase {
    public ConfigBase(String identifier) {
        configPath = "config" + File.separator +identifier+ ".prop";
        propertyBase = new Properties();
        File configFile = new File(MinecraftClient.getInstance().runDirectory, configPath);
        if (configFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(configFile);
                propertyBase.load(fis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        loadConfig0();
    }

    private void loadConfig0() {
        Arrays.stream(this.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            ConfigEntry annotation = field.getAnnotation(ConfigEntry.class);
            try {

                System.out.println("Loading: "+field.getName());
                System.out.println(field.getType().getName());
                if (field.getType() == Double.TYPE) {
                    field.set(this, getDoubleOrDefault(annotation.key(), annotation.doubleDefault()));
                }
                if (field.getType() == Float.TYPE) {
                    field.set(this,(float)getDoubleOrDefault(annotation.key(), annotation.doubleDefault()));
                }
                if (field.getType() == Boolean.TYPE) {
                    field.set(this,getBooleanOrDefault(annotation.key(), annotation.booleanDefault()));
                }
                if (field.getType() == Integer.TYPE) {
                    field.set(this,(int)getLongOrDefault(annotation.key(), annotation.intDefault()));
                }
                if (field.getType() == Long.TYPE) {
                    field.set(this,getLongOrDefault(annotation.key(), annotation.intDefault()));
                }
                if (field.getType() == String.class) {
                    field.set(this,propertyBase.getProperty(annotation.key(),annotation.stringDefault()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean getBooleanOrDefault(String key, boolean defaultValue) {
        String value = propertyBase.getProperty(key, defaultValue ? "yes" : "no");
        return value.equals("yes");
    }

    private double getDoubleOrDefault(String key, double defaultValue) {
        String value = propertyBase.getProperty(key, defaultValue + "");
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private long getLongOrDefault(String key, long defaultValue) {
        String value = propertyBase.getProperty(key, defaultValue + "");
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
    private void putBoolean(String key, boolean b) {
        propertyBase.remove(key);
        propertyBase.put(key, b ? "yes" : "no");
    }

    private void putDouble(String key, double b) {
        propertyBase.remove(key);
        propertyBase.put(key, b + "");
    }
    private void putLong(String key,long b){
        propertyBase.remove(key);
        propertyBase.put(key, b + "");
    }
    private String configPath ;
    private Properties propertyBase;

    private void saveConfig0() {
        Arrays.stream(this.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            ConfigEntry annotation = field.getAnnotation(ConfigEntry.class);
            try {
                if (field.getType() == Double.TYPE) {
                    putDouble(annotation.key(),field.getDouble(this));
                }
                if (field.getType() == Float.TYPE) {
                    putDouble(annotation.key(),field.getFloat(this));
                }
                if (field.getType() == Boolean.TYPE) {
                    putBoolean(annotation.key(),field.getBoolean(this));
                }
                if (field.getType() == Integer.TYPE) {
                    putLong(annotation.key(),field.getInt(this));
                }
                if (field.getType() == Long.TYPE) {
                    putLong(annotation.key(),field.getLong(this));
                }
                if (field.getType() == String.class) {
                    propertyBase.put(annotation.key(),field.get(this).toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveConfig() {
        saveConfig0();
        File configFile = new File(MinecraftClient.getInstance().runDirectory, configPath);
        if (configFile.exists()) {
            configFile.delete();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            propertyBase.store(fos, "");
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
