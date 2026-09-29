package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.annotations.SinceVersion;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.MobVisibilityComponent;
import fr.maxlego08.menu.api.loader.ItemComponentLoader;
import fr.maxlego08.menu.api.utils.resolvable.Resolvable;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableFloat;
import fr.maxlego08.menu.api.utils.resolvable.paper.ResolvableRegistryKeySet;
import fr.maxlego08.menu.api.utils.resolvable.paper.TagKeySetResolvable;
import fr.maxlego08.menu.zcore.logger.Logger;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@AutoComponentLoader
@SinceVersion("26.3")
public final class MobVisibilityItemComponentLoader extends ItemComponentLoader {

    public MobVisibilityItemComponentLoader() {
        super("mob-visibility");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        if (componentSection == null) return null;

        Object rawEntityTypes = componentSection.get("targeting-entity-types");
        Resolvable<RegistryKeySet<EntityType>> entityTypes;
        if (rawEntityTypes instanceof String entityType && entityType.startsWith("#")) {
            entityTypes = TagKeySetResolvable.of(RegistryKey.ENTITY_TYPE, entityType);
        } else {
            entityTypes = ResolvableRegistryKeySet.typedKeySetOrNull(RegistryKey.ENTITY_TYPE, rawEntityTypes);
        }

        ResolvableFloat visibility = this.asResolvableFloat(componentSection, "visibility");
        if (entityTypes == null || visibility == null) {
            Logger.info("The mob-visibility component at " + path + " in " + file.getName() + " needs targeting-entity-types and visibility, it is ignored.", Logger.LogType.WARNING);
            return null;
        }

        Float fixedVisibility = visibility.getResolvedValue();
        if (fixedVisibility != null && (fixedVisibility < 0 || fixedVisibility > 10)) {
            Logger.info("visibility of the mob-visibility component at " + path + " in " + file.getName() + " must be between 0 and 10.", Logger.LogType.WARNING);
            visibility = ResolvableFloat.of(Math.clamp(fixedVisibility, 0f, 10f));
        }

        return new MobVisibilityComponent(entityTypes, visibility);
    }
}
