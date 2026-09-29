package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.SuspiciousStewEffectsComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacySuspiciousStewEffectsComponent;
import fr.maxlego08.menu.api.utils.resolvable.bukkit.ResolvablePotionEffect;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

@AutoComponentLoader
public class SuspiciousStewEffectsItemComponentLoader extends AbstractEffectItemComponentLoader {

    public SuspiciousStewEffectsItemComponentLoader() {
        super("suspicious-stew-effects");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        List<Map<?, ?>> effects = configuration.getMapList(path);
        List<ResolvablePotionEffect> resolvablePotionEffects = this.parseResolvablePotionEffects(effects);
        if (resolvablePotionEffects.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new SuspiciousStewEffectsComponent(resolvablePotionEffects)
                : new LegacySuspiciousStewEffectsComponent(resolvablePotionEffects);
    }
}
