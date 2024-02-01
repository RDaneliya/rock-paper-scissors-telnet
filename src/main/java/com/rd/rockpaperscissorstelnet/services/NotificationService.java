package com.rd.rockpaperscissorstelnet.services;

import com.rd.rockpaperscissorstelnet.domain.Player;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void notifyGameWon(Player player) throws InterruptedException {
        sendMessageToPlayer(player, "Congrats! You won");
    }

    public void notifyGameLost(Player player) throws InterruptedException {
        sendMessageToPlayer(player, "You lost :(");
    }

    public void notifyPlayerTied(Player player) throws InterruptedException {
        sendMessageToPlayer(player, "Tie! One more round");
    }

    public void notifyGameStarted(Player player, String opponentName) throws InterruptedException {
        sendMessageToPlayer(player, "You will play against " + opponentName);
        sendMessageToPlayer(player, "Please type r(rock), p(paper) or s(scissors)");
    }

    public void notifyOpponentLoggedOut(Player player) throws InterruptedException {
        sendMessageToPlayer(player, "Your opponent logged out. Session will be terminated");
    }

    public void notifyAlreadyMadeAction(Player player) throws InterruptedException {
        sendMessageToPlayer(player, "Can't change action");
    }

    private void sendMessageToPlayer(Player player, String playerMessage) throws InterruptedException {
        player.getChannel()
              .writeAndFlush(playerMessage + "\r\n")
              .sync();
    }
}
