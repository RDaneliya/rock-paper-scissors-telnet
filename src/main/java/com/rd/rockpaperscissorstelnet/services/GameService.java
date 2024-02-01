package com.rd.rockpaperscissorstelnet.services;

import com.rd.rockpaperscissorstelnet.domain.GameSession;
import com.rd.rockpaperscissorstelnet.domain.Player;
import com.rd.rockpaperscissorstelnet.domain.PlayerAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    private final NotificationService notificationService;
    private final ChannelService channelService;
    private final Queue<Player> freePlayers = new LinkedList<>();
    private final Map<UUID, GameSession> gameSessions = new HashMap<>();

    public void removePlayer(Player player) throws InterruptedException {
        freePlayers.remove(player);
        GameSession gameSession = gameSessions.remove(player.getGameSessionId());
        Player anotherPlayer = gameSession.getAnotherPlayer(player);
        notificationService.notifyOpponentLoggedOut(anotherPlayer);
        channelService.logout(anotherPlayer);
    }

    public void startMatchmaking(Player player) throws InterruptedException {
        if (freePlayers.isEmpty()) {
            freePlayers.add(player);
            return;
        }
        Player opponent = freePlayers.poll();
        GameSession gameSession = new GameSession();
        gameSession.addPlayer(player);
        gameSession.addPlayer(opponent);
        UUID sessionId = UUID.randomUUID();
        assignSession(player, opponent, sessionId);
        gameSessions.put(sessionId, gameSession);
        notificationService.notifyGameStarted(player, opponent.getUsername());
        notificationService.notifyGameStarted(opponent, player.getUsername());
    }

    public boolean isInGame(Player player) {
        return gameSessions.containsKey(player.getGameSessionId());
    }

    public void makeChoice(Player player, PlayerAction playerAction) throws InterruptedException {
        GameSession playerSession = gameSessions.get(player.getGameSessionId());

        if (playerSession.getPlayerActions().get(player) != null) {
            notificationService.notifyAlreadyMadeAction(player);
            return;
        }

        playerSession.getPlayerActions().merge(player, playerAction, (k, v) -> playerAction);

        if (!playerSession.isAllActionsDone()) {
            return;
        }

        Player winner = getWinner(playerSession);

        if (winner == null) {
            Player anotherPlayer = playerSession.getAnotherPlayer(player);
            notificationService.notifyPlayerTied(player);
            notificationService.notifyPlayerTied(anotherPlayer);
            playerSession.resetSession();
            return;
        }

        Player notWinner = playerSession.getAnotherPlayer(winner);
        notificationService.notifyGameWon(winner);
        notificationService.notifyGameLost(notWinner);

        gameSessions.remove(winner.getGameSessionId());
        channelService.logout(winner);
        channelService.logout(notWinner);
    }

    public Player getWinner(GameSession gameSession) {
        Map.Entry<Player, PlayerAction> firstEntry = gameSession.getFirstEntry();
        Map.Entry<Player, PlayerAction> secondEntry = gameSession.getLastEntry();
        Player firstPlayer = firstEntry.getKey();
        Player secondPlayer = firstEntry.getKey();
        PlayerAction firstPlayerAction = firstEntry.getValue();
        PlayerAction secondPlayerAction = secondEntry.getValue();

        if (firstPlayerAction.equals(secondPlayerAction)) {
            return null;
        }
        switch (firstPlayerAction) {
            case ROCK -> {
                return secondPlayerAction.equals(PlayerAction.SCISSORS) ? firstPlayer : secondPlayer;
            }
            case PAPER -> {
                return secondPlayerAction.equals(PlayerAction.ROCK) ? firstPlayer : secondPlayer;
            }
            case SCISSORS -> {
                return secondPlayerAction.equals(PlayerAction.PAPER) ? firstPlayer : secondPlayer;
            }
            default -> throw new IllegalStateException();
        }
    }

    private void assignSession(Player player1, Player player2, UUID sessionId) {
        player1.setGameSessionId(sessionId);
        player2.setGameSessionId(sessionId);
    }
}
