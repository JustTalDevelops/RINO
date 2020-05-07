package com.justtal.RIPGSDDI;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Watcher extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        User user = event.getAuthor();
        Message message = event.getMessage();
        GuildWatcher watcher = Main.guildWatcherHashMap.get(guild);
        watcher.addMessage(user, message);
        System.out.println("[DEBUG] Checking if user has delay");
        if (watcher.hasDelay(user)) {
            System.out.println("[DEBUG] User has delay");
            long lastTime = watcher.getLastDelay(user);
            long nowTime = System.currentTimeMillis();
            long difference = nowTime - lastTime;
            System.out.println("[DEBUG] Difference is " + difference);
            if (watcher.hasDifference(user)) {
                System.out.println("[DEBUG] User has past distance");
                long lastDifference = watcher.getLastDifference(user);
                long fullDifference = difference - lastDifference;
                if (watcher.hasLastFullDifference(user)) {
                    long lastFullDifference = watcher.getLastFullDifference(user);
                    watcher.setLastFullDifference(user, fullDifference);
                    System.out.println("[DEBUG] Past difference is " + lastDifference);
                    System.out.println("[DEBUG] Full difference is " + fullDifference);
                    System.out.println("[DEBUG] Last full difference is " + lastFullDifference);
                    if (fullDifference >= (lastFullDifference - 250) && fullDifference <= (lastFullDifference + 250)) {
                        System.out.println("[DEBUG] Differences matched!");
                        watcher.updateDelay(user);
                        System.out.println("[DEBUG] Checking if user has correct delays");
                        if (watcher.getCorrectDelays(user) == 2) {
                            System.out.println("[DEBUG] User has correct delays, kicking!");
                            user.openPrivateChannel().complete().sendMessage("You were kicked by RINO for being a spam bot. If this assumption was incorrect, contact `justtal@protonmail.com`.").queue();
                            guild.kick(guild.getMember(user), "Automatic kick issued by RINO.").queue();
                            event.getChannel().sendMessage("A user in this chat was kicked automatically by RINO.").queue();
                            watcher.setCorrectDelays(user, 0);
                            watcher.resetUser(user);
                        }
                    }
                } else {
                    watcher.setLastFullDifference(user, fullDifference);
                }
            }

            System.out.println("[DEBUG] Updating difference");
            watcher.putDifference(user, difference);
        } else {
            System.out.println("[DEBUG] User does not have past distance");
            watcher.putDelay(user, System.currentTimeMillis());
        }
    }

}