package fr.maxlego08.menu.api.loader;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Documentation: <a href="https://docs.zmenu.dev/api/create-material-loader">here</a>
 * <p>The MaterialLoader interface defines methods for loading an {@link ItemStack} from a configuration.</p>
 */
public abstract class MaterialLoader {

    private final String key;

    public MaterialLoader(String key) {
        this.key = key;
    }

    /**
     * Returns the key used to load the ItemStack.
     *
     * @return The key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Loads an ItemStack based on the provided configuration.
     *
     * @param player         The player.
     * @param configuration  The current configuration.
     * @param path           The current path.
     * @param materialString The material as a String.
     * @return The loaded ItemStack.
     */
    public abstract ItemStack load(Player player, YamlConfiguration configuration, String path, String materialString);

}
