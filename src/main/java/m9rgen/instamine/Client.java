package m9rgen.instamine;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

public class Client implements ModMenuApi {
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(net.minecraft.network.chat.Component.literal("Instamine Config"));

            ConfigCategory category = builder.getOrCreateCategory(
                net.minecraft.network.chat.Component.literal("Blocks")
            );

            ConfigEntryBuilder entry = builder.entryBuilder();
            BooleanListEntry enabled = entry.startBooleanToggle(
                net.minecraft.network.chat.Component.literal("Enabled"),
                Config.enabled
            )
            .setDefaultValue(true)
            .setSaveConsumer(value -> {
                Config.enabled = value;
                save(Config.blocks);
            })
            .build();

            Requirement requirement = Requirement.isTrue(enabled);

            category.addEntry(enabled);

            category.addEntry(
                entry.startFloatField(
                    net.minecraft.network.chat.Component.literal("Hardness"),
                    Config.hardness
                )
                .setDefaultValue(1.5f)
                .setMin(0.1f)
                .setRequirement(requirement)
                .setSaveConsumer(value -> {
                    Config.hardness = value;
                    save(Config.blocks);
                })
                .build()
            );

            category.addEntry(
                entry.startStrList(
                    net.minecraft.network.chat.Component.literal("Blocks"),
                    new ArrayList<>(Config.blocks)
                )
                .setDefaultValue(new ArrayList<>(Config.DEFAULT_BLOCKS))
                .setInsertInFront(true)
                .setExpanded(true)
                .setRequirement(requirement)
                .setSaveConsumer(Client::save)
                .build()
            );

            category.addEntry(
                entry.startBooleanToggle(
                    net.minecraft.network.chat.Component.literal("Ores"),
                    Config.ores
                )
                .setDefaultValue(true)
                .setRequirement(requirement)
                .setSaveConsumer(value -> {
                    Config.ores = value;
                    save(Config.blocks);
                })
                .build()
            );

            category.addEntry(
                entry.startBooleanToggle(
                    net.minecraft.network.chat.Component.literal("Logs"),
                    Config.logs
                )
                .setDefaultValue(false)
                .setRequirement(requirement)
                .setSaveConsumer(value -> {
                    Config.logs = value;
                    save(Config.blocks);
                })
                .build()
            );

            category.addEntry(
                entry.startBooleanToggle(
                    net.minecraft.network.chat.Component.literal("Planks"),
                    Config.planks
                )
                .setDefaultValue(false)
                .setRequirement(requirement)
                .setSaveConsumer(value -> {
                    Config.planks = value;
                    save(Config.blocks);
                })
                .build()
            );
            category.addEntry(
                entry.startTextDescription(
                    net.minecraft.network.chat.Component.literal("Thanks for using Instamine :)")
                    .withStyle(style -> style.withColor(0xFFFF00))
                )
                .build()
            );

            return builder.build();
        };
    }

    private static void save(List<String> blocks) {
        if (Config.save(FabricLoader.getInstance().getConfigDir(), blocks)) {
            Instamine.applyConfig();
        }
    }
}