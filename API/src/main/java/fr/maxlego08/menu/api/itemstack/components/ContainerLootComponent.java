package fr.maxlego08.menu.api.itemstack.components;

import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.context.BuildContext;
import fr.maxlego08.menu.api.itemstack.ItemComponent;
import fr.maxlego08.menu.api.utils.ItemUtil;
import fr.maxlego08.menu.api.utils.resolvable.Resolvable;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableEnum;
import fr.maxlego08.menu.api.utils.resolvable.lang.ResolvableLong;
import fr.maxlego08.menu.zcore.logger.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ContainerLootComponent extends ItemComponent {

    private final @Nullable ResolvableEnum<LootTables> resolvableLootTable;
    private final @NotNull ResolvableLong seed;

    public ContainerLootComponent(@Nullable ResolvableEnum<LootTables> resolvableLootTable, @NotNull ResolvableLong seed) {
        this.resolvableLootTable = resolvableLootTable;
        this.seed = seed;
    }

    public @Nullable ResolvableEnum<LootTables> getResolvableLootTable() {
        return this.resolvableLootTable;
    }

    public @NotNull ResolvableLong getSeed() {
        return this.seed;
    }

    @Override
    public void apply(@NotNull BuildContext context, @NotNull ItemStack itemStack, @Nullable Player player) {
        boolean apply = ItemUtil.editMeta(itemStack, BlockStateMeta.class, blockStateMeta -> {
            if (blockStateMeta instanceof Lootable lootableMeta) {

                Resolvable.applyResolvable(context, this.resolvableLootTable, lootTables -> {
                   lootableMeta.setLootTable(lootTables.getLootTable());
                });

                Resolvable.applyResolvable(context, this.seed, lootableMeta::setSeed);

            }
        });
        if (!apply && Configuration.enableDebug) {
            Logger.info("Failed to apply ContainerLootComponent to itemStack: " + itemStack.getType().name() + ". This item does not support block state meta.");
        }
    }

    @Override
    public @Nullable Object serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (this.resolvableLootTable != null) map.put("loot-table", this.resolvableLootTable.serialize());
        if (this.seed.isDynamic() || !Long.valueOf(0L).equals(this.seed.getResolvedValue())) {
            map.put("seed", this.seed.serialize());
        }
        return map;
    }

}
