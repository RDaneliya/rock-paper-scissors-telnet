package com.rd.rockpaperscissorstelnet.services;

import com.rd.rockpaperscissorstelnet.domain.Player;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final Map<Player, Channel> loggedUsers = new HashMap<>();
//    private final

    public boolean login(Player player, Channel channel) {
        if (!loggedUsers.containsKey(player)) {
            loggedUsers.put(player, channel);
            return true;
        }
        return false;
    }

    public boolean isLoggedIn(Channel channel) {
        return loggedUsers.containsValue(channel);
    }

    public void logout(Channel channel) {
        loggedUsers.values().remove(channel);
        channel.close();
    }

    public void logout(Player player) {
        loggedUsers.remove(player);
        Channel channel = player.getChannel();
        channel.close();
    }

    public Player getChannelUser(Channel channel) {
        return loggedUsers.entrySet()
                          .stream()
                          .filter(entry -> channel.equals(entry.getValue()))
                          .map(Map.Entry::getKey)
                          .findAny()
                          .orElseGet(() -> {
                              logout(channel);
                              return null;
                          });
    }
}
