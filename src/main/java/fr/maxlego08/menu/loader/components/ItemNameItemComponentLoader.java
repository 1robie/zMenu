package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.ItemNameComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyItemNameComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableString;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class ItemNameItemComponentLoader extends ItemComponentLoader {

    public ItemNameItemComponentLoader(){
        super("item-name");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        String name = configuration.getString(path);
        if (name == null) {
            return null;
        }
        ResolvableString itemName = ResolvableString.autoOrNull(name);
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new ItemNameComponent(itemName)
                : new LegacyItemNameComponent(itemName);
    }
}
