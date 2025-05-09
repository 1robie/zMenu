package fr.maxlego08.menu.listener;

import fr.maxlego08.menu.ZMenuPlugin;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.configuration.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * Allows to use the key to change item in his second hand, by default the key will be F
 */
public class SwapKeyListener implements Listener {
    @EventHandler
    public void onPressKey(PlayerSwapHandItemsEvent event) {
        if (Config.useSwapItemOffHandKeyToOpenMainMenu) {
            InventoryManager inventoryManager = ZMenuPlugin.getInstance().getInventoryManager();
            Player player = event.getPlayer();

            if (Config.useSwapItemOffHandKeyToOpenMainMenuNeedsShift) {
                if (player.isSneaking()) {
                    inventoryManager.openInventory(player, Config.mainMenu);
                    event.setCancelled(true);
                }
            } else {
                inventoryManager.openInventory(player, Config.mainMenu);
                event.setCancelled(true);
            }
        }
    }
}
