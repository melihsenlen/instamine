package m9rgen.instamine;

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
                entryBuilder.startBooleanToggle(
                    net.minecraft.network.chat.Component.literal("Enabled"),
                    Instamine.enabled
                )
                .setDefaultValue(true)
                .setSaveConsumer(value -> {
                    Instamine.enabled = value;
                    Instamine.saveConfig(FabricLoader.getInstance().getConfigDir(), Instamine.blocks);
                })
                .build()
            );

            category.addEntry(
                entryBuilder.startFloatField(
                    net.minecraft.network.chat.Component.literal("Hardness"),
                    Instamine.hardness
                )
                .setDefaultValue(1.5f)
                .setMin(0.1f)
                .setMax(100f)
                .setSaveConsumer(value -> {
                    Instamine.hardness = value;
                    Instamine.saveConfig(FabricLoader.getInstance().getConfigDir(), Instamine.blocks);
                })
                .build()
            );

            category.addEntry(
                entryBuilder.startStrList(
                    net.minecraft.network.chat.Component.literal("Blocks"),
                    new ArrayList<>(Instamine.blocks)
                )
                .setDefaultValue(new ArrayList<>(Instamine.DEFAULTS))
                .setInsertInFront(true)
                .setExpanded(true)
                .setSaveConsumer(newList -> Instamine.saveConfig(
                    FabricLoader.getInstance().getConfigDir(), newList
                ))
                .build()
            );

            category.addEntry(
                entryBuilder.startTextDescription(
                    net.minecraft.network.chat.Component.literal("Thanks for using Instamine!")
                    .withStyle(style -> style.withColor(0xFFFF00))
                )
                .build()
            );

            return builder.build();
        };
    }
}