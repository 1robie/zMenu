package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.annotations.UntilVersion;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.MapColorComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyMapColorComponent;
import fr.maxlego08.menu.api.utils.resolvable.bukkit.ResolvableColor;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
@UntilVersion("26.2")
public class MapColorItemComponentLoader extends AbstractColorItemComponentLoader {

    public MapColorItemComponentLoader(){
        super("map-color");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        Object o = configuration.get(path);
        if (o == null) return null;
        ResolvableColor resolvableColor = ResolvableColor.of(o);
        if (resolvableColor == null) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new MapColorComponent(resolvableColor)
                : new LegacyMapColorComponent(resolvableColor);
    }
}
