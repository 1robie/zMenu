package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.ContainerLootComponent;
import fr.maxlego08.menu.api.itemstack.components.legacy.LegacyContainerLootComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableEnum;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableLong;
import fr.maxlego08.menu.api.utils.version.MinecraftVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
public class ContainerLootItemComponentLoader extends ItemComponentLoader {

    public ContainerLootItemComponentLoader(){
        super("container-loot");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        if (componentSection == null) return null;ResolvableEnum<LootTables> lootTablesResolvable = ResolvableEnum.autoOrNull(LootTables.class, componentSection.getString("loot-table"));
        ResolvableLong seed = this.asResolvableLong(componentSection, "seed", 0L);
        return MinecraftVersion.isServerAtLeast("1.21.3")
                ? new ContainerLootComponent(lootTablesResolvable, seed)
                : new LegacyContainerLootComponent(lootTablesResolvable, seed);
    }
}
