package com.rd.rockpaperscissorstelnet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    private NavigableMap<Player, PlayerAction> playerActions = new TreeMap<>();

    public void addPlayer(Player player) {
        playerActions.put(player, null);
    }

    public boolean isAllActionsDone() {
        return playerActions.size() > 1 && !playerActions.containsValue(null);
    }

    public Map.Entry<Player, PlayerAction> getFirstEntry() {
        return playerActions.firstEntry();
    }

    public Map.Entry<Player, PlayerAction> getLastEntry() {
        return playerActions.lastEntry();
    }

    public Player getAnotherPlayer(Player player) {
        Player firstPlayer = getFirstEntry().getKey();
        if (firstPlayer.equals(player)) {
            return getLastEntry().getKey();
        }
        return firstPlayer;
    }

    public void resetSession() {
        playerActions.clear();
    }


}
