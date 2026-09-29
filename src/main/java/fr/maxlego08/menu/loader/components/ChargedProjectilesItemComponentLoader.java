package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.ChargedProjectilesComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyChargedProjectilesComponent;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

@AutoComponentLoader
public class ChargedProjectilesItemComponentLoader extends AbstractMenuItemStackListComponentLoaderBase {

    public ChargedProjectilesItemComponentLoader(MenuPlugin plugin){
        super("charged-projectiles", plugin);
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        List<Map<?, ?>> mapList = configuration.getMapList(path);
        List<MenuItemStack> projectiles = this.loadItemStackList(mapList, file);
        if (projectiles.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new ChargedProjectilesComponent(projectiles)
                : new LegacyChargedProjectilesComponent(projectiles);
    }
}
