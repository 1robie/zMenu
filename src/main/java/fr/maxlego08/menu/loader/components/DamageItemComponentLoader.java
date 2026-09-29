package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.DamageComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyDamageComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableInt;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class DamageItemComponentLoader extends ItemComponentLoader {

    public DamageItemComponentLoader(){
        super("damage");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        ResolvableInt damages = this.asResolvableInt(configuration, path);
        if (damages != null) {
            return MinecraftVersion.isServerAtLeast("1.21.3")
                    ? new DamageComponent(damages)
                    : new LegacyDamageComponent(damages);
        }
        return null;
    }
}
