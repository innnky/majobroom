package net.fabricmc.majobroom.utils;


import com.google.gson.Gson;
//import net.fabricmc.loom.configuration.providers.minecraft.assets.MinecraftAssetsProvider;
import net.fabricmc.majobroom.MajoBroom;
import net.fabricmc.majobroom.items.BroomItem;
import net.fabricmc.majobroom.jsonbean.GeomtryBean;
import net.fabricmc.majobroom.jsonbean.ModelBean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.*;

public class ModelJsonReader {
    public ModelJsonReader() {
    }

    public static GeomtryBean readJson(String path) {
        try {
            InputStream in = null;
            var optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier("majobroom",path));
//            in = list

            if (optionalResource.isPresent()) {
                in = optionalResource.get().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer stringBuffer = new StringBuffer();
                String temp = "";

                while((temp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(temp);
                }

                String presonsString = stringBuffer.toString();
                Gson gson = new Gson();
                ModelBean fromJson = (ModelBean)gson.fromJson(presonsString, ModelBean.class);
                return fromJson.getModel();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return null;
    }
}
