package com.justtal.RIPGSDDI;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class NewServers extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Main.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, Main.jda.getGuilds().size() + " servers from incoming raids."));

        Guild guild = event.getGuild();
        System.out.println("[DEBUG] Created GuildWatcher for Guild: " + guild.getName());
        Main.guildWatcherHashMap.put(guild, new GuildWatcher(guild));
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        Main.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, Main.jda.getGuilds().size() + " servers from incoming raids."));

        Guild guild = event.getGuild();
        System.out.println("[DEBUG] Deleted GuildWatcher for Guild: " + guild.getName());
        Main.guildWatcherHashMap.remove(guild);
    }

}