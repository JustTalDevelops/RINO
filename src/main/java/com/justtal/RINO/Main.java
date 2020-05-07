package com.justtal.RIPGSDDI;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main {

    public static JDA jda;

    public static HashMap<Guild, GuildWatcher> guildWatcherHashMap = new HashMap<>();

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = new JDABuilder()
                .setToken("<TOKEN>")

                .addEventListeners(new Watcher())
                .addEventListeners(new NewServers())
                .build();

        jda.awaitReady();

        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, jda.getGuilds().size() + " servers from incoming raids."));

        for (Guild guild : jda.getGuilds()) {
            System.out.println("[DEBUG] Created GuildWatcher for Guild: " + guild.getName());
            guildWatcherHashMap.put(guild, new GuildWatcher(guild));
        }
    }

}
