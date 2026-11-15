package m9rgen.instamine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Instamine implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("instamine");

    // Replaced as a whole on every apply(), so the game never reads an incomplete set.
    public static volatile Set<Block> BLOCKS = Set.of();

    private static final List<String> ORES = List.of(
        "coal ore", "deepslate coal ore",
        "iron ore", "deepslate iron ore",
        "copper ore", "deepslate copper ore",
        "gold ore", "deepslate gold ore", "nether gold ore",
        "redstone ore", "deepslate redstone ore",
        "emerald ore", "deepslate emerald ore",
        "lapis ore", "deepslate lapis ore",
        "diamond ore", "deepslate diamond ore",
        "nether quartz ore"
    );

    private static final List<String> LOGS = List.of(
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
    );

    private static final List<String> PLANKS = List.of(
        "oak planks", "spruce planks", "birch planks", "jungle planks",
        "acacia planks", "dark oak planks", "mangrove planks", "cherry planks",
        "pale oak planks", "bamboo planks", "poplar planks",
        "crimson planks", "warped planks"
    );

    @Override
    public void onInitialize() {
        Config.load(FabricLoader.getInstance().getConfigDir());
        Config.blocks = clean(Config.blocks);
        apply();
        LOGGER.info("Instamine loaded");
    }

    // Rebuilds BLOCKS from the current config.
    public static void apply() {
        Map<String, Identifier> ids = ids();
        Set<Block> set = new HashSet<>();

        if (Config.ores)   add(set, ids, ORES);
        if (Config.logs)   add(set, ids, LOGS);
        if (Config.planks) add(set, ids, PLANKS);
        add(set, ids, Config.blocks);

        BLOCKS = Set.copyOf(set);
    }

    // Turns typed names into the standard form used in the config, and drops unknown blocks.
    public static List<String> clean(List<String> names) {
        Map<String, Identifier> ids = ids();
        Set<String> clean = new LinkedHashSet<>();

        for (String name : names) {
            Identifier id = find(ids, name);
            if (id != null) clean.add(id.getPath().replace('_', ' '));
        }
        return new ArrayList<>(clean);
    }

    private static void add(Set<Block> set, Map<String, Identifier> ids, List<String> names) {
        for (String name : names) {
            Identifier id = find(ids, name);

            if (id == null) {
                LOGGER.warn("Unknown block '{}', skipping", name);
                continue;
            }
            BuiltInRegistries.BLOCK.get(id).ifPresent(holder -> set.add(holder.value()));
        }
    }

    private static Map<String, Identifier> ids() {
        Map<String, Identifier> ids = new HashMap<>();

        for (Identifier id : BuiltInRegistries.BLOCK.keySet()) {
            String key = squash(id.getPath());
            if (id.getNamespace().equals("minecraft") || !ids.containsKey(key)) ids.put(key, id);
        }
        return ids;
    }

    private static Identifier find(Map<String, Identifier> ids, String name) {
        if (name == null) return null;
        return ids.get(squash(name.substring(name.indexOf(':') + 1)));
    }

    private static String squash(String name) {
        return name.toLowerCase(Locale.ROOT).replace(" ", "").replace("_", "");
    }
}