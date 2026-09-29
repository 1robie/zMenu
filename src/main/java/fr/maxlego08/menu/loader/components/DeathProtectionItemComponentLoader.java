package fr.maxlego08.menu.loader.components;

import fr.maxlego08.menu.api.annotations.AutoComponentLoader;
import fr.maxlego08.menu.api.annotations.SinceVersion;
import fr.maxlego08.menu.api.context.MenuItemStackContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.itemstack.components.DeathProtectionComponent;
import fr.maxlego08.menu.api.utils.resolvable.paper.PaperResolvableConsumeEffect;
import fr.maxlego08.menu.api.utils.resolvable.paper.PaperResolvableDeathProtection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

@AutoComponentLoader
@SinceVersion("1.21.3")
public class DeathProtectionItemComponentLoader extends AbstractEffectItemComponentLoader {

    public DeathProtectionItemComponentLoader(){
        super("death-protection");
    }

    @Override
    public @Nullable ItemComponent load(@NotNull MenuItemStackContext context, @NotNull File file, @NotNull YamlConfiguration configuration, @NotNull String path, @Nullable ConfigurationSection componentSection) {
        if (componentSection == null) return null;
        List<PaperResolvableConsumeEffect> resolvablePotionEffects = this.parseEffects(componentSection.getMapList("death-effects"));
        return resolvablePotionEffects.isEmpty() ? null : new DeathProtectionComponent(new PaperResolvableDeathProtection(resolvablePotionEffects));
    }
}
