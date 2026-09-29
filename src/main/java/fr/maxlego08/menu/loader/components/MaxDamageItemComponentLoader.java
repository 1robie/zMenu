package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.MaxDamageComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyMaxDamageComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableInt;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class MaxDamageItemComponentLoader extends ItemComponentLoader {

    public MaxDamageItemComponentLoader(){
        super("max-damage");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        ResolvableInt maxDamage = this.asResolvableInt(configuration, path);
        if (maxDamage == null) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new MaxDamageComponent(maxDamage)
                : new LegacyMaxDamageComponent(maxDamage);
    }
}
