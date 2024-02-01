package com.rd.rockpaperscissorstelnet.domain;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
public class Player implements Comparable<Player>{

    private final String username;
    private final Channel channel;
    private UUID gameSessionId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Player player)) return false;
        return Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return username.compareTo(otherPlayer.username);
    }
}
