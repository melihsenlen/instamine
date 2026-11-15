package m9rgen.instamine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // JSON file structure:
    public static final class Data {
        public List<String> blocks = List.of("deepslate", "end stone", "cobblestone", "cobbled deepslate");
        public float hardness = 1.5f;
        public boolean enabled = true;
        public boolean ores = true;
        public boolean logs = false;
        public boolean planks = false;
    }

    public static final Data DEFAULT = new Data();

    public static List<String> blocks = DEFAULT.blocks;
    public static float hardness = DEFAULT.hardness;
    public static boolean enabled = DEFAULT.enabled;
    public static boolean ores = DEFAULT.ores;
    public static boolean logs = DEFAULT.logs;
    public static boolean planks = DEFAULT.planks;

    private static Path file;

    private Config() {}

    public static void load(Path dir) {
        file = dir.resolve("instamine.json");

        if (!Files.exists(file)) {
            save();
            return;
        }

        try {
            Data data = GSON.fromJson(Files.readString(file), Data.class);
            if (data == null) return;

            blocks = data.blocks != null ? data.blocks : DEFAULT.blocks;
            hardness = data.hardness > 0 ? data.hardness : DEFAULT.hardness;
            enabled = data.enabled;
            ores = data.ores;
            logs = data.logs;
            planks = data.planks;
        } catch (IOException | JsonParseException e) {
            Instamine.LOGGER.error("Failed to read config, using defaults", e);
        }
    }

    public static void save() {
        Data data = new Data(); 
        data.blocks = blocks;
        data.hardness = hardness;
        data.enabled = enabled;
        data.ores = ores;
        data.logs = logs;
        data.planks = planks;

        try {
            Files.writeString(file, GSON.toJson(data));
        } catch (IOException e) {
            Instamine.LOGGER.error("Failed to save config", e);
        }
    }
}