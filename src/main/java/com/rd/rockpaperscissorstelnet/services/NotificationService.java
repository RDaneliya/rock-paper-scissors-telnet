package com.rd.rockpaperscissorstelnet.services;

import com.rd.rockpaperscissorstelnet.domain.Player;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void notifyGameWon(Player player) throws InterruptedException {
        sendMessageToPlayers(player, "Congrats! You won");
    }

    public void notifyGameLost(Player player) throws InterruptedException {
        sendMessageToPlayers(player, "You lost :(");
    }

    public void notifyPlayerTied(Player player) throws InterruptedException {
        sendMessageToPlayers(player, "Tie! One more round");
    }

    public void notifyGameStarted(Player player, String opponentName) throws InterruptedException {
        player.getChannel()
              .writeAndFlush("You will play against " + opponentName + "\r\n")
              .sync();
        player.getChannel()
              .writeAndFlush("Please type r(rock), p(paper) or s(scissors)" + "\r\n")
              .sync();
    }

    public void notifyOpponentLoggedOut(Player player) {
        player.getChannel()
                .writeAndFlush("Your opponent logged out. Session will be terminated");
    }

    private void sendMessageToPlayers(Player player, String playerMessage) throws InterruptedException {
        player.getChannel()
              .writeAndFlush(playerMessage + "\r\n")
              .sync();
    }
}
