package m9rgen.instamine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public final class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger("instamine");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String NAME = "instamine.json";

    public static final float DEFAULT_HARDNESS = 1.5f;
    public static final List<String> DEFAULT_BLOCKS = List.of(
        "deepslate",
        "end stone",
        "cobblestone",
        "cobbled deepslate"
    );

    public static List<String> blocks = DEFAULT_BLOCKS;
    public static float hardness = DEFAULT_HARDNESS;
    public static boolean enabled = true;

    public static boolean ores   = true;
    public static boolean logs   = false;
    public static boolean planks = false;

    private Config() {}

    private record Data(
        List<String> blocks,
        float hardness,
        Boolean enabled,
        Boolean ores,
        Boolean logs,
        Boolean planks
    ) {}

    public static void load(Path dir) {
        Path file = dir.resolve(NAME);

        if (Files.exists(file)) {
            try {
                Data data = GSON.fromJson(Files.readString(file), Data.class);
                blocks   = data.blocks()   != null ? data.blocks()   : DEFAULT_BLOCKS;
                hardness = data.hardness() > 0     ? data.hardness() : DEFAULT_HARDNESS;
                enabled  = data.enabled()  != null ? data.enabled()  : true;

                ores     = data.ores()     != null ? data.ores()     : true;
                logs     = data.logs()     != null ? data.logs()     : false;
                planks   = data.planks()   != null ? data.planks()   : false;
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to read config, using defaults", e);
                blocks = DEFAULT_BLOCKS;
            }
        } else {
            blocks = DEFAULT_BLOCKS;
            try {
                Files.createDirectories(dir);
                write(file, DEFAULT_BLOCKS);
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to write default config", e);
            }
        }
    }

    public static boolean save(Path dir, List<String> newBlocks) {
        try {
            write(dir.resolve(NAME), newBlocks);
            blocks = newBlocks;
            return true;
        } catch (IOException e) {
            LOGGER.error("Instamine: failed to save config", e);
            return false;
        }
    }

    private static void write(Path dir, List<String> blocks) throws IOException {
        Files.writeString(dir, GSON.toJson(
            new Data(blocks, hardness, enabled, ores, logs, planks)
        ));
    }
}