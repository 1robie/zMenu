package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.CustomNameComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyCustomNameComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.api.utils.PaperMetaUpdater;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableString;
import fr.maxlego08.menu.api.utils.resolvable.paper.ResolvableComponent;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class CustomNameItemComponentLoader extends ItemComponentLoader {
    private final MetaUpdater metaUpdater;

    public CustomNameItemComponentLoader(MenuPlugin plugin){
        super("custom-name");
        this.metaUpdater = plugin.getMetaUpdater();
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        String customName = configuration.getString(path);
        if (this.metaUpdater instanceof PaperMetaUpdater paperMetaUpdater && MinecraftVersion.isServerAtLeast("1.21.3")) {
            ResolvableComponent resolvableComponent = ResolvableComponent.autoOrNull(customName, paperMetaUpdater);
            return resolvableComponent != null ? new CustomNameComponent(resolvableComponent) : null;
        }
        ResolvableString resolvableString = ResolvableString.autoOrNull(customName);
        return resolvableString != null ? new LegacyCustomNameComponent(resolvableString) : null;
    }
}
