package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.ResolvablePersistentDataEntry;
import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.CustomDataComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyCustomDataComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AutoComponentLoader
public class CustomDataItemComponentLoader extends ItemComponentLoader {

    public CustomDataItemComponentLoader() {
        super("custom-data");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        if (componentSection == null) return null;

        List<ResolvablePersistentDataEntry> pdcEntries = new ArrayList<>();

        for (String key : componentSection.getKeys(false)) {
            Object value = componentSection.get(key);
            if (value == null) continue;

            ResolvablePersistentDataEntry entry = ResolvablePersistentDataEntry.fromKeyValue(key, value);
            if (entry != null) {
                pdcEntries.add(entry);
            }
        }

        if (pdcEntries.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.4")
                ? new CustomDataComponent(pdcEntries)
                : new LegacyCustomDataComponent(pdcEntries);
    }
}
