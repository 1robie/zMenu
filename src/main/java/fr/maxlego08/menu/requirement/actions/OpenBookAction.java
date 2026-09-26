package fr.maxlego08.menu.requirement.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.common.utils.ActionHelper;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class OpenBookAction extends ActionHelper {

    private final String title;
    private final String author;
    private final List<String> lines;
    private final Map<?, List<String>> pages;

    public OpenBookAction(String title, String author, List<String> lines) {
        this(title, author, lines, toPages(lines));
    }

    /**
     * @param pages the raw "lines" section as read from the configuration (page key to its lines), kept for serialization.
     */
    public OpenBookAction(String title, String author, List<String> lines, Map<?, List<String>> pages) {
        this.title = title;
        this.author = author;
        this.lines = lines;
        this.pages = pages;
    }

    private static Map<?, List<String>> toPages(List<String> lines) {
        Map<Integer, List<String>> pages = new LinkedHashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            pages.put(i + 1, Arrays.asList(lines.get(i).split(Pattern.quote("<newline>"), -1)));
        }
        return pages;
    }

    @Override
    protected void execute(@NonNull Player player, Button button, @NonNull InventoryEngine inventory, @NonNull Placeholders placeholders) {
        inventory.getPlugin().getMetaUpdater().openBook(player, this.papi(this.title, player), this.papi(this.author, player), this.papi(this.lines, player));
    }

    @Override
    protected void serializeProperties(@NonNull Map<String, Object> map) {
        map.put("type", "book");
        map.put("title", this.title);
        map.put("author", this.author);
        if (!this.pages.isEmpty()) map.put("lines", this.pages);
    }
}
