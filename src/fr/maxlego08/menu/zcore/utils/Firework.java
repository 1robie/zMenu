package fr.maxlego08.menu.zcore.utils;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

public class Firework {
    private boolean isStar;
    private FireworkEffect effect;

    /**
     * Create a firework
     *
     * @param isStar use {@link Material#FIREWORK_STAR} or not
     * @param effect the firework effect
     */
    public Firework(boolean isStar, FireworkEffect effect) {
        this.isStar = isStar;
        this.effect = effect;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public FireworkEffect getEffect() {
        return effect;
    }

    public void setEffect(FireworkEffect effect) {
        this.effect = effect;
    }

    @NotNull
    public ItemStack toItemStack(int amount) {
        Material material = this.isStar ? Material.FIREWORK_STAR : Material.FIREWORK_ROCKET;
        ItemStack itemStack = new ItemStack(material, amount);
        if (this.isStar) {
            FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemStack.getItemMeta();
            fireworkEffectMeta.setEffect(effect);
            itemStack.setItemMeta(fireworkEffectMeta);
        } else {
            FireworkMeta fireworkMeta = (FireworkMeta) itemStack.getItemMeta();
            fireworkMeta.addEffect(effect);
            itemStack.setItemMeta(fireworkMeta);
        }
        return itemStack;
    }
}
