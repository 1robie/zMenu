package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.MapIdComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyMapIdComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableInt;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class MapIdItemComponentLoader extends ItemComponentLoader {

    public MapIdItemComponentLoader(){
        super("map-id");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        ResolvableInt mapId = this.asResolvableInt(configuration, path);
        if (mapId == null) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new MapIdComponent(mapId)
                : new LegacyMapIdComponent(mapId);
    }
}
