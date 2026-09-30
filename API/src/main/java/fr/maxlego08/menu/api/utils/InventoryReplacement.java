package fr.maxlego08.menu.api.utils;

import fr.maxlego08.menu.api.Inventory;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record InventoryReplacement(String inventoryName, String plugin, List<Integer> pages) {
    public static final String DEFAULT_PLUGIN = "zMenu";

    public InventoryReplacement(String inventoryName, String plugin, List<Integer> pages) {
        this.inventoryName = inventoryName;
        this.plugin = plugin == null || plugin.equalsIgnoreCase("") ? DEFAULT_PLUGIN : plugin;
        this.pages = pages;
    }

    public boolean shouldTrigger(Inventory inventory, int page) {

        // Inventory Name
        if (!Objects.equals(this.inventoryName, inventory.getFileName())) {
            return false;
        }
        // Plugin Name
        if (!Objects.equals(this.plugin, inventory.getPlugin().getName())) {
            return false;
        }

        // Page
        return this.pages.isEmpty() || this.pages.contains(page);
    }

    public void serialize(@NotNull ConfigurationSection section) {
        section.set("name", this.inventoryName);
        if (!DEFAULT_PLUGIN.equals(this.plugin)) section.set("plugin", this.plugin);
        if (!this.pages.isEmpty()) section.set("pages", this.pages);
    }
}