package com.rd.rockpaperscissorstelnet.handler;

import com.rd.rockpaperscissorstelnet.domain.PlayerAction;
import com.rd.rockpaperscissorstelnet.domain.Player;
import com.rd.rockpaperscissorstelnet.services.ChannelService;
import com.rd.rockpaperscissorstelnet.services.GameService;
import com.rd.rockpaperscissorstelnet.utils.MessageUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@ChannelHandler.Sharable
public class GameInputHandler extends ChannelInboundHandlerAdapter {
    private final ChannelService channelService;
    private final GameService gameService;

    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.fireChannelActive();
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext context, @NotNull Object message) throws InterruptedException {
        Channel channel = context.channel();
        Player currentPlayer = channelService.getChannelUser(channel);
        if (currentPlayer == null) {
            log.debug("User not logged in");
            return;
        }

        if(!gameService.isInGame(currentPlayer)){
            return;
        }


        switch (message.toString()) {
            case "r" -> gameService.makeChoice(currentPlayer, PlayerAction.ROCK);
            case "p" -> gameService.makeChoice(currentPlayer, PlayerAction.PAPER);
            case "s" -> gameService.makeChoice(currentPlayer, PlayerAction.SCISSORS);
            default -> MessageUtils.sendMessageLn(context, "Incorrect input");
        }
        log.debug(currentPlayer + ": " + message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }
}
