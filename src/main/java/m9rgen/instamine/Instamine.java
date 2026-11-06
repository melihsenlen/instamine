package m9rgen.instamine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class Instamine implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("instamine");

    public static final Set<Block> BLOCKS = new HashSet<>();
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
            "nether quartz ore"
        ),
        
        "logs", List.of(
            "oak log", "spruce log", "birch log", "jungle log",
            "acacia log", "dark oak log", "mangrove log", "cherry log",
            "pale oak log", "poplar log",

            "oak wood", "spruce wood", "birch wood",
            "jungle wood", "acacia wood", "dark oak wood", "mangrove wood",
            "cherry wood", "pale oak wood", "poplar wood",

            "stripped oak log", "stripped spruce log", "stripped birch log",
            "stripped jungle log", "stripped acacia log", "stripped dark oak log",
            "stripped mangrove log", "stripped cherry log", "stripped pale oak log",
            "stripped poplar log",

            "stripped oak wood", "stripped spruce wood", "stripped birch wood",
            "stripped jungle wood", "stripped acacia wood", "stripped dark oak wood",
            "stripped mangrove wood", "stripped cherry wood", "stripped pale oak wood",
            "stripped poplar wood",

            "crimson stem", "warped stem",
            "crimson hyphae", "warped hyphae",

            "stripped crimson stem", "stripped warped stem",
            "stripped crimson hyphae", "stripped warped hyphae"
        ),

        "planks", List.of(
            "oak planks", "spruce planks", "birch planks", "jungle planks",
            "acacia planks", "dark oak planks", "mangrove planks", "cherry planks",
            "pale oak planks", "bamboo planks", "poplar planks",
            "crimson planks", "warped planks"
        )
    );

    @Override
    public void onInitialize() {
        Config.load(FabricLoader.getInstance().getConfigDir());
        applyConfig();
        LOGGER.info("Instamine loaded");
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

    private static void addGroup(String key) {
        for (String member : GROUPS.get(key)) {
            String resolved = parseBlock(member);

            if (resolved == null) {
                LOGGER.warn("Instamine: group '{}' member '{}' not found, skipping", key, member);
                continue;
            }
            BuiltInRegistries.BLOCK.get(Identifier.parse(resolved))
                .ifPresent(h -> BLOCKS.add(h.value()));
        }
    }

    public static void applyConfig() {
        BLOCKS.clear();
        List<String> cleaned = new ArrayList<>();

        if (Config.ores)   {addGroup("ores");}
        if (Config.logs)   {addGroup("logs");}
        if (Config.planks) {addGroup("planks");}

        for (String entry : Config.blocks) {
            String key = entry.toLowerCase().trim();

            if (GROUPS.containsKey(key)) {continue;}
            String resolved = parseBlock(entry);
            
            if (resolved == null) {
                LOGGER.warn("Instamine: could not resolve '{}', skipping", entry);
                continue;
            }
            BuiltInRegistries.BLOCK.get(Identifier.parse(resolved))
                .ifPresent(h -> BLOCKS.add(h.value()));
            cleaned.add(prettyBlock(resolved));
        }
        Config.blocks = cleaned;
    }
}