package com.rd.rockpaperscissorstelnet.handler;

import com.rd.rockpaperscissorstelnet.domain.Player;
import com.rd.rockpaperscissorstelnet.services.ChannelService;
import com.rd.rockpaperscissorstelnet.services.GameService;
import com.rd.rockpaperscissorstelnet.utils.MessageUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class GameLoginHandler extends ChannelInboundHandlerAdapter {
    private final ChannelService channelService;
    private final GameService gameService;

    @Override
    public void channelActive(ChannelHandlerContext context) {
        MessageUtils.sendMessageLn(context, "Hello, please type your login");
        context.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws InterruptedException {
        Channel channel = context.channel();
        if (channelService.isLoggedIn(channel)) {
            context.fireChannelRead(message);
            return;
        }

        String stringMessage = (String) message;

        Player currentPlayer = new Player(stringMessage, channel, null);
        boolean isSuccessful = channelService.login(currentPlayer, channel);
        if (!isSuccessful) {
            MessageUtils.sendMessageLn(context, "Login is already taken.");
            log.debug("User with name {} already exists", currentPlayer.getUsername());
            return;
        }

        log.debug("Logged as " + stringMessage);
        MessageUtils.sendMessageLn(context, "Welcome " + stringMessage);

        if (currentPlayer.getGameSessionId() == null) {
            MessageUtils.sendMessageLn(context, "Looking for a worthy opponent...");
            gameService.startMatchmaking(currentPlayer);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        Player player = channelService.getChannelUser(context.channel());
        if (player != null) {
            gameService.removePlayer(player);
        }
        channelService.logout(context.channel());
    }
}
