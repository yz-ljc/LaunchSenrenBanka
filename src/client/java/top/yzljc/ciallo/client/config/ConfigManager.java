package top.yzljc.ciallo.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("yzljc_launchapp_config.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigManager INSTANCE;

    public List<String> gameList = new ArrayList<>();
    public boolean isFullScreen = false; // 新增全屏设置

    public static ConfigManager get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ConfigManager.class);
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new ConfigManager();
            }
        } else {
            INSTANCE = new ConfigManager();
        }

        if (INSTANCE.gameList == null) {
            INSTANCE.gameList = new ArrayList<>();
        }

        if (INSTANCE.gameList.isEmpty()) {
            INSTANCE.gameList.add("1144400");
            save();
        }
    }

    public static void save() {
        if (INSTANCE == null) INSTANCE = new ConfigManager();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}