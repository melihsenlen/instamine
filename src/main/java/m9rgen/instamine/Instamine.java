package m9rgen.instamine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instamine implements ModInitializer {
    public static final String MOD_ID = "instamine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<Block> BLOCK_SET = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final List<String> DEFAULTS = List.of(
        "deepslate",
        "end stone",
        "cobblestone"
    );

    public static List<String> blocks = DEFAULTS;
    public static float hardness = 1.5f;
    public static Boolean enabled = true;

    @Override
    public void onInitialize() {
        loadConfig(FabricLoader.getInstance().getConfigDir());
        LOGGER.info("Instamine loaded");
    }

    public static void loadConfig(Path configDir) {
        Path configFile = configDir.resolve("instamine.json");

        if (Files.exists(configFile)) {
            try {
                String json = Files.readString(configFile);
                Config config = GSON.fromJson(json, Config.class);
                blocks = config.blocks != null ? config.blocks : DEFAULTS;
                hardness = config.hardness > 0 ? config.hardness : 1.5f;
                enabled = config.enabled != null ? config.enabled : true;
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to read config, using defaults", e);
                blocks = DEFAULTS;
            }
        } else {
            blocks = DEFAULTS;
            try {
                Files.createDirectories(configDir);
                Files.writeString(configFile, GSON.toJson(new Config(DEFAULTS, 1.5f, true)));
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to write default config", e);
            }
        }
        applyConfig();
    }

    public static void saveConfig(Path configDir, List<String> newBlocks) {
        Path configFile = configDir.resolve("instamine.json");
        try {
            Files.writeString(configFile, GSON.toJson(new Config(newBlocks, hardness, enabled)));
            blocks = newBlocks;
            applyConfig();
        } catch (IOException e) {
            LOGGER.error("Instamine: failed to save config", e);
        }
    }

    private static String parseBlock(String input) {
    String name = input.contains(":") ? input.split(":", 2)[1] : input;
    String normalized = name.toLowerCase().replaceAll("[\\s_]", "");
    
    for (Identifier loc : BuiltInRegistries.BLOCK.keySet()) {
        if (loc.getPath().replaceAll("_", "").equals(normalized)) {
            return loc.toString();
        }
    }
    return null;
    }

    private static String prettyBlock(String resolved) {
        String path = resolved.contains(":") ? resolved.split(":", 2)[1] : resolved;
        return path.replace("_", " ");
    }

    public static void applyConfig() {
        BLOCK_SET.clear();
        List<String> cleaned = new ArrayList<>();

        for (String name : blocks) {
            String resolved = parseBlock(name);
            if (resolved == null) {
                LOGGER.warn("Instamine: could not resolve '{}', skipping", name);
                continue;
            }
            BuiltInRegistries.BLOCK.get(Identifier.parse(resolved)).ifPresent(h -> BLOCK_SET.add(h.value()));
            cleaned.add(prettyBlock(resolved));
        }
        blocks = cleaned;
    }

    private record Config(List<String> blocks, float hardness, Boolean enabled) {}
}