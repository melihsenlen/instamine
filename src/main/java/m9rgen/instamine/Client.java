package m9rgen.instamine;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Client implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Instamine Config"))
                .setSavingRunnable(Client::save);

            ConfigCategory category = builder.getOrCreateCategory(Component.literal("General"));
            ConfigEntryBuilder entry = builder.entryBuilder();

            BooleanListEntry enabled = entry.startBooleanToggle(Component.literal("Enabled"), Config.enabled)
                .setDefaultValue(Config.DEFAULT.enabled)
                .setSaveConsumer(value -> Config.enabled = value)
                .build();

            // Everything except the master switch is greyed out while it's off.
            Requirement requirement = Requirement.isTrue(enabled);
            category.addEntry(enabled);

            category.addEntry(
                entry.startFloatField(Component.literal("Hardness"), Config.hardness)
                    .setDefaultValue(Config.DEFAULT.hardness)
                    .setMin(0.1f)
                    .setRequirement(requirement)
                    .setSaveConsumer(value -> Config.hardness = value)
                    .build()
            );

            category.addEntry(
                entry.startStrList(Component.literal("Blocks"), new ArrayList<>(Config.blocks))
                    .setDefaultValue(new ArrayList<>(Config.DEFAULT.blocks))
                    .setInsertInFront(true)
                    .setExpanded(true)
                    .setRequirement(requirement)
                    .setSaveConsumer(value -> Config.blocks = value)
                    .build()
            );

            category.addEntry(toggle(entry, "Ores", Config.ores, Config.DEFAULT.ores, requirement, value -> Config.ores = value));
            category.addEntry(toggle(entry, "Logs", Config.logs, Config.DEFAULT.logs, requirement, value -> Config.logs = value));
            category.addEntry(toggle(entry, "Planks", Config.planks, Config.DEFAULT.planks, requirement, value -> Config.planks = value));

            category.addEntry(
                entry.startTextDescription(
                    Component.literal("Thanks for using Instamine :)")
                        .withStyle(style -> style.withColor(0xFFFF00))
                ).build()
            );

            return builder.build();
        };
    }

    private static BooleanListEntry toggle(
        ConfigEntryBuilder entry,
        String name,
        boolean value,
        boolean fallback,
        Requirement requirement,
        Consumer<Boolean> setter
    ) {
        return entry.startBooleanToggle(Component.literal(name), value)
            .setDefaultValue(fallback)
            .setRequirement(requirement)
            .setSaveConsumer(setter)
            .build();
    }

    private static void save() {
        Config.blocks = Instamine.clean(Config.blocks);
        Config.save();
        Instamine.apply();
    }
}