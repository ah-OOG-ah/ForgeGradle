package net.minecraftforge.gradle.json;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.minecraftforge.gradle.json.LiteLoaderJson.VersionObject;
import net.minecraftforge.gradle.json.version.AssetIndex;
import net.minecraftforge.gradle.json.version.ManifestVersion;
import net.minecraftforge.gradle.json.version.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonFactory {
    public static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new EnumAdaptorFactory());
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(File.class, new FileAdapter());
        builder.registerTypeAdapter(VersionObject.class, new LiteLoaderJson.VersionAdapter());
        builder.registerTypeAdapter(new TypeToken<Map<String, ManifestVersion>>() {}.getType(), new MojangManifestAdapter());
        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        GSON = builder.create();
    }

    public static Version loadVersion(File json, String mcVersion, File inheritanceDir) throws JsonSyntaxException, JsonIOException, IOException {
        FileReader reader = new FileReader(json);
        Version v = GSON.fromJson(reader, Version.class);
        reader.close();

        if (!Strings.isNullOrEmpty(v.inheritsFrom)) {
            File parentFile = new File(inheritanceDir, v.inheritsFrom + ".json");
            if (!parentFile.exists()) {
                throw new FileNotFoundException("Inherited json file (" + v.inheritsFrom + ") not found! Maybe you are running in offline mode?");
            }
            Version parent = loadVersion(new File(inheritanceDir, v.inheritsFrom + ".json"), mcVersion, inheritanceDir);
            v.extendFrom(parent);
        } else if (v.assetIndex == null) { // inherit if the assetIndex is missing
            File parentFile = new File(inheritanceDir, mcVersion + ".json");
            if (!parentFile.exists()) {
                throw new FileNotFoundException("Inherited json file (" + v.inheritsFrom + ") not found! Maybe you are running in offline mode?");
            }
            if (parentFile != json) {
                Version parent = loadVersion(new File(inheritanceDir, mcVersion + ".json"), mcVersion, inheritanceDir);
                v.extendFrom(parent);
            }
        }

        return v;
    }

    public static AssetIndex loadAssetsIndex(File json) throws JsonSyntaxException, JsonIOException, IOException {
        FileReader reader = new FileReader(json);
        AssetIndex a = GSON.fromJson(reader, AssetIndex.class);
        reader.close();
        return a;
    }

    public static LiteLoaderJson loadLiteLoaderJson(File json) throws JsonSyntaxException, JsonIOException, IOException {
        FileReader reader = new FileReader(json);
        LiteLoaderJson a = GSON.fromJson(reader, LiteLoaderJson.class);
        reader.close();
        return a;
    }

    public static Map<String, MCInjectorStruct> loadMCIJson(File json) throws IOException {
        FileReader reader = new FileReader(json);
        Map<String, MCInjectorStruct> ret = new LinkedHashMap<String, MCInjectorStruct>();

        JsonObject object = (JsonObject) new JsonParser().parse(reader);
        reader.close();

        for (Entry<String, JsonElement> entry : object.entrySet()) {
            ret.put(entry.getKey(), GSON.fromJson(entry.getValue(), MCInjectorStruct.class));
        }
        return ret;
    }
}
