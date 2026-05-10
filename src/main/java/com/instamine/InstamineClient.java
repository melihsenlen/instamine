package com.instamine;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.ClientModInitializer;

import java.util.ArrayList;

public class InstamineClient implements ModMenuApi, ClientModInitializer {

    @Override
    public void onInitializeClient() {
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(net.minecraft.network.chat.Component.literal("Instamine Config"));

            ConfigCategory category = builder.getOrCreateCategory(
                net.minecraft.network.chat.Component.literal("Blocks")
            );

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            category.addEntry(
                entryBuilder.startStrList(
                    net.minecraft.network.chat.Component.literal("Affected Blocks"),
                    new ArrayList<>(Instamine.blocks)
                )
                .setDefaultValue(new ArrayList<>(Instamine.DEFAULTS))
                .setTooltip(net.minecraft.network.chat.Component.literal("Block IDs that will mine at stone speed"))
                .setSaveConsumer(newList -> Instamine.saveConfig(
                    FabricLoader.getInstance().getConfigDir(), newList
                ))
                .build()
            );
            return builder.build();
        };
    }
}