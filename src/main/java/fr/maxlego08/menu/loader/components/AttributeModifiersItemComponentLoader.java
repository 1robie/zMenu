package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.attribute.AttributeMergeStrategy;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.AttributeModifiersComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyAttributeModifiersComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.bukkit.ResolvableAttributeWrapper;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableEnum;
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
public class AttributeModifiersItemComponentLoader extends ItemComponentLoader {
    private final MenuPlugin plugin;

    public AttributeModifiersItemComponentLoader(MenuPlugin plugin){
        super("attribute-modifiers");
        this.plugin = plugin;
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        if (componentSection == null) return null;
        String mergeStrategyStr = componentSection.getString("attribute-merge-strategy");
        ResolvableEnum<AttributeMergeStrategy> attributeMergeStrategyResolvable = ResolvableEnum.autoOrNull(AttributeMergeStrategy.class, mergeStrategyStr);
        List<Map<?, ?>> mapList = componentSection.getMapList("modifiers");
        List<ResolvableAttributeWrapper> resolvableWrappers = new ArrayList<>();
        for (Map<?, ?> rawMap : mapList) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) rawMap;
            ResolvableAttributeWrapper wrapper = ResolvableAttributeWrapper.fromMap(map);
            if (wrapper != null) {
                resolvableWrappers.add(wrapper);
            }
        }
        if (resolvableWrappers.isEmpty()) return null;
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new AttributeModifiersComponent(this.plugin, resolvableWrappers, attributeMergeStrategyResolvable)
                : new LegacyAttributeModifiersComponent(this.plugin, resolvableWrappers, attributeMergeStrategyResolvable);
    }
}
