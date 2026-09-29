package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.OminousBottleAmplifierComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyOminousBottleAmplifierComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableInt;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class OminousBottleAmplifierItemComponentLoader extends ItemComponentLoader {

    public OminousBottleAmplifierItemComponentLoader(){
        super("ominous-bottle-amplifier");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        ResolvableInt amplifier = this.asResolvableInt(configuration, path);
        if (amplifier == null) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new OminousBottleAmplifierComponent(amplifier)
                : new LegacyOminousBottleAmplifierComponent(amplifier);
    }
}
