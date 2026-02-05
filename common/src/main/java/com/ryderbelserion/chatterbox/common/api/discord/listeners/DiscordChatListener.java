package com.ryderbelserion.chatterbox.common.api.discord.listeners;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class DiscordChatListener extends ListenerAdapter {

    private final ConfigManager configManager;

    public DiscordChatListener(@NotNull final ChatterBoxPlugin instance) {
        this.configManager = instance.getConfigManager();
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        if (event.isWebhookMessage() || !event.isFromGuild()) {
            return;
        }

        final User user = event.getAuthor();

        if (user.isBot() || user.isSystem()) {
            return;
        }

        final DiscordConfig config = this.configManager.getDiscord();

        if (!config.isPlayerAlertsEnabled()) {
            return;
        }

        final Member member = event.getMember();

        if (member == null) {
            return;
        }

        final PlayerAlertConfig alertConfig = config.getAlertConfig();

        final Message message = event.getMessage();

        final Channel channel = event.getChannel();

        final String id = channel.getId();

        alertConfig.sendMinecraft(member, id, message.getContentStripped(), PlayerAlert.DC_CHAT_ALERT, Map.of(
                "{message_raw_format}", message.getContentRaw(),
                "{message_display_format}", message.getContentDisplay()
        ));
    }
}