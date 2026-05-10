package com.instamine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instamine implements ModInitializer {
    public static final String MOD_ID = "instamine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<Block> BLOCK_SET = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final List<String> DEFAULTS = List.of(
        "minecraft:deepslate",
        "minecraft:cobbled_deepslate",
        "minecraft:polished_deepslate",
        "minecraft:deepslate_bricks",
        "minecraft:cracked_deepslate_bricks",
        "minecraft:deepslate_tiles",
        "minecraft:cracked_deepslate_tiles",
        "minecraft:chiseled_deepslate",
        "minecraft:deepslate_coal_ore",
        "minecraft:deepslate_iron_ore",
        "minecraft:deepslate_gold_ore",
        "minecraft:deepslate_redstone_ore",
        "minecraft:deepslate_emerald_ore",
        "minecraft:deepslate_lapis_ore",
        "minecraft:deepslate_diamond_ore",
        "minecraft:deepslate_copper_ore",
        "minecraft:end_stone",
        "minecraft:end_stone_bricks"
    );

    public static List<String> blocks = DEFAULTS;

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
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to read config, using defaults", e);
                blocks = DEFAULTS;
            }
        } else {
            blocks = DEFAULTS;
            try {
                Files.createDirectories(configDir);
                Files.writeString(configFile, GSON.toJson(new Config(DEFAULTS)));
            } catch (IOException e) {
                LOGGER.error("Instamine: failed to write default config", e);
            }
        }
        applyConfig();
    }

    public static void saveConfig(Path configDir, List<String> newBlocks) {
    Path configFile = configDir.resolve("instamine.json");
    try {
        Files.writeString(configFile, GSON.toJson(new Config(newBlocks)));
        blocks = newBlocks;
        applyConfig();
    } catch (IOException e) {
        LOGGER.error("Instamine: failed to save config", e);
    }
}

    public static void applyConfig() {
        BLOCK_SET.clear();
        for (String id : blocks) {
            Identifier loc = Identifier.tryParse(id);
            if (loc == null) {
                LOGGER.warn("Instamine: invalid block id '{}', skipping", id);
                continue;
            }
            BuiltInRegistries.BLOCK.get(loc).ifPresent(h -> BLOCK_SET.add(h.value()));
        }
    }
    private record Config(List<String> blocks) {}
}