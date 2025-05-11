package fr.maxlego08.menu.button.loader;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuPlugin;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.button.buttons.ZNextButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class NextLoader extends ButtonLoader {

    private final InventoryManager manager;

    public NextLoader(MenuPlugin plugin) {
        super(plugin, "next");
        this.manager = plugin.getInventoryManager();
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        defaultButtonValue.setPermanent(true);
        return new ZNextButton(this.manager);
    }

}
