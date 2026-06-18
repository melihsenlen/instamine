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
import java.util.Map;

public class Instamine implements ModInitializer {
    public static final String MOD_ID = "instamine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<Block> BLOCK_SET = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Map<String, List<String>> GROUPS = Map.of(
        "ores", List.of(
            "coal ore", "deepslate coal ore",
            "iron ore", "deepslate iron ore",
            "copper ore", "deepslate copper ore",
            "gold ore", "deepslate gold ore", "nether gold ore",
            "redstone ore", "deepslate redstone ore",
            "emerald ore", "deepslate emerald ore",
            "lapis ore", "deepslate lapis ore",
            "diamond ore", "deepslate diamond ore",
            "ancient debris", "nether quartz ore"
        ),
        "logs", List.of(
            "oak log", "spruce log", "birch log", "jungle log",
            "acacia log", "dark oak log", "mangrove log", "cherry log",
            "oak wood", "spruce wood", "birch wood", "jungle wood",
            "acacia wood", "dark oak wood", "mangrove wood", "cherry wood",
            "stripped oak log", "stripped spruce log", "stripped birch log",
            "stripped jungle log", "stripped acacia log", "stripped dark oak log",
            "stripped mangrove log", "stripped cherry log",
            "stripped oak wood", "stripped spruce wood", "stripped birch wood",
            "stripped jungle wood", "stripped acacia wood", "stripped dark oak wood",
            "stripped mangrove wood", "stripped cherry wood"
        )
    );

    public static final List<String> DEFAULTS = List.of(
        "deepslate",
        "end stone",
        "cobblestone",
        "cobbled deepslate",
        "ores",
        "logs"
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

        for (String entry : blocks) {
            String key = entry.toLowerCase().trim();

            if (GROUPS.containsKey(key)) {
                cleaned.add(key);

                for (String member : GROUPS.get(key)) {
                    String resolved = parseBlock(member);

                    if (resolved == null) {
                        LOGGER.warn("Instamine: group '{}' member '{}' not found, skipping", key, member);
                        continue;
                    }
                    BuiltInRegistries.BLOCK.get(Identifier.parse(resolved))
                        .ifPresent(h -> BLOCK_SET.add(h.value()));
                }
                continue;
            }
            String resolved = parseBlock(entry);
            
            if (resolved == null) {
                LOGGER.warn("Instamine: could not resolve '{}', skipping", entry);
                continue;
            }
            BuiltInRegistries.BLOCK.get(Identifier.parse(resolved))
                .ifPresent(h -> BLOCK_SET.add(h.value()));
            cleaned.add(prettyBlock(resolved));
        }
        blocks = cleaned;
    }

    private record Config(List<String> blocks, float hardness, Boolean enabled) {}
}