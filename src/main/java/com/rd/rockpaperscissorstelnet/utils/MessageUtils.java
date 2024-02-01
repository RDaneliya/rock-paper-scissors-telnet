package com.rd.rockpaperscissorstelnet.utils;

import io.netty.channel.ChannelHandlerContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtils {
    public static void sendMessageLn(ChannelHandlerContext context, String message){
        context.writeAndFlush(message + "\r\n");
    }
}
