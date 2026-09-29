package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.BundleContentsComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyBundleContentsComponent;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

@AutoComponentLoader
public class BundleContentsItemComponentLoader extends AbstractMenuItemStackListComponentLoaderBase {

    public BundleContentsItemComponentLoader(MenuPlugin plugin){
        super("bundle-contents", plugin);
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);

        List<Map<?, ?>> mapList = configuration.getMapList(path);
        List<MenuItemStack> contents = this.loadItemStackList(mapList, file);
        if (contents.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new BundleContentsComponent(contents)
                : new LegacyBundleContentsComponent(contents);
    }
}
