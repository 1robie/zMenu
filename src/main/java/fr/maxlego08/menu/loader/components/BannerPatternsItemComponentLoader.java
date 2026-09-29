package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.BannerPatternsComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyBannerPatternsComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.bukkit.ResolvableBannerPattern;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoComponentLoader
public class BannerPatternsItemComponentLoader extends ItemComponentLoader {

    public BannerPatternsItemComponentLoader(){
        super("banner-patterns");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        path = this.normalizePath(path);
        List<Map<?, ?>> rawPatterns = configuration.getMapList(path);
        List<ResolvableBannerPattern> resolvablePatterns = new ArrayList<>();
        for (var rawPattern : rawPatterns) {
            @SuppressWarnings("unchecked")
            Map<String, Object> patternMap = (Map<String, Object>) rawPattern;
            ResolvableBannerPattern resolvable = ResolvableBannerPattern.fromMap(patternMap);
            if (resolvable != null) {
                resolvablePatterns.add(resolvable);
            }
        }
        if (resolvablePatterns.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new BannerPatternsComponent(resolvablePatterns)
                : new LegacyBannerPatternsComponent(resolvablePatterns);
    }
}
