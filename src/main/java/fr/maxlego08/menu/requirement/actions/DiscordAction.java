package fr.maxlego08.menu.requirement.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.configuration.Configuration;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.common.utils.ActionHelper;
import fr.maxlego08.menu.zcore.logger.Logger;
import fr.maxlego08.menu.zcore.utils.discord.DiscordConfiguration;
import fr.maxlego08.menu.zcore.utils.discord.DiscordWebhook;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public class DiscordAction extends ActionHelper {

    private final DiscordConfiguration configuration;
    private final List<Map<?, ?>> rawEmbeds;

    public DiscordAction(DiscordConfiguration configuration) {
        this(configuration, null);
    }

    public DiscordAction(DiscordConfiguration configuration, List<Map<?, ?>> rawEmbeds) {
        this.configuration = configuration;
        this.rawEmbeds = rawEmbeds;
    }

    @Override
    protected void execute(@NonNull Player player, Button button, @NonNull InventoryEngine inventory, @NonNull Placeholders placeholders) {

        var scheduler = inventory.getPlugin().getScheduler();
        DiscordWebhook discordWebhook = new DiscordWebhook(this.configuration.webhookUrl());
        this.configuration.apply(text -> text == null ? null : player == null ? text : this.papi(placeholders.parse(text), player), discordWebhook);

        scheduler.runAsync(w -> {
            try {
                discordWebhook.execute();
            } catch (Exception exception) {
                if (Configuration.enableDebug) {
                    Logger.error(exception);
                }
            }
        });
    }

    @Override
    protected void serializeProperties(@NonNull Map<String, Object> map) {
        List<?> embeds = this.configuration.embeds();
        boolean hasEmbeds = embeds != null && !embeds.isEmpty();
        if (hasEmbeds && this.rawEmbeds == null) {
            throw new UnsupportedOperationException("The discord action cannot be serialized: its embeds were given without their original configuration");
        }
        map.put("type", "discord");
        map.put("webhook", this.configuration.webhookUrl());
        if (this.configuration.avatarUrl() != null) map.put("avatar", this.configuration.avatarUrl());
        if (this.configuration.content() != null) map.put("message", this.configuration.content());
        if (this.configuration.username() != null) map.put("username", this.configuration.username());
        if (this.rawEmbeds != null && !this.rawEmbeds.isEmpty()) map.put("embeds", this.rawEmbeds);
    }
}
