package com.justtal.RIPGSDDI;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildWatcher {

    private Guild guild;

    private HashMap<User, Long> userWithDelayMap = new HashMap<>();

    private HashMap<User, Long> differenceMap = new HashMap<>();

    private HashMap<User, Long> lastFullDifferenceMap = new HashMap<>();

    private HashMap<User, Integer> userWithAmountOfDelaysCorrectMap = new HashMap<>();

    private HashMap<User, List<Message>> messageMap = new HashMap<>();

    public GuildWatcher(Guild guild) {
        this.guild = guild;
    }

    // Delay Manager

    public boolean hasDelay(User user) {
        return userWithDelayMap.containsKey(user);
    }

    public long getLastDelay(User user) {
        return userWithDelayMap.get(user);
    }

    public void putDelay(User user, long currentTimeMillis) {
        userWithDelayMap.put(user, currentTimeMillis);
    }

    // Difference Manager

    public boolean hasDifference(User user) {
        return differenceMap.containsKey(user);
    }

    public void putDifference(User user, long difference) {
        differenceMap.put(user, difference);
    }

    public long getLastDifference(User user) {
        return differenceMap.get(user);
    }

    // Delays Correct Manager

    public void updateDelay(User user) {
        if (userWithAmountOfDelaysCorrectMap.containsKey(user)) {
            int value = userWithAmountOfDelaysCorrectMap.get(user);
            userWithAmountOfDelaysCorrectMap.put(user, value + 1);
        } else {
            userWithAmountOfDelaysCorrectMap.put(user, 1);
        }
    }

    public int getCorrectDelays(User user) {
        return userWithAmountOfDelaysCorrectMap.get(user);
    }

    public void setCorrectDelays(User user, int delay) {
        userWithAmountOfDelaysCorrectMap.put(user, delay);
    }

    // Last Full Difference Manager

    public boolean hasLastFullDifference(User user) {
        return lastFullDifferenceMap.containsKey(user);
    }

    public long getLastFullDifference(User user) {
        return lastFullDifferenceMap.get(user);
    }

    public void setLastFullDifference(User user, long fullDistance) {
        lastFullDifferenceMap.put(user, fullDistance);
    }

    public void resetUser(User user) {
        lastFullDifferenceMap.remove(user);
        userWithAmountOfDelaysCorrectMap.remove(user);
        differenceMap.remove(user);
        userWithDelayMap.remove(user);

        List<Message> messages = getMessages(user);
        for (Message message : messages) {
            try {
                Message newMessage = guild.getTextChannelById(message.getChannel().getId()).retrieveMessageById(message.getId()).complete();
                if (newMessage != null) newMessage.delete();
            } catch (Exception e) { }
        }
    }

    // Message Manager

    public List<Message> getMessages(User user) {
        return messageMap.get(user);
    }

    public void addMessage(User user, Message message) {
        if (messageMap.containsKey(user)) {
            List<Message> messages = messageMap.get(user);
            messages.add(message);
            messageMap.put(user, messages);
        } else {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            messageMap.put(user, messages);
        }
    }

}