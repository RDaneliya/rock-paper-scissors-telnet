package com.rd.rockpaperscissorstelnet.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final GameInputHandler simpleChatServerHandler;
    private final GameLoginHandler gameLoginHandler;
    private final StringEncoder stringEncoder;
    private final StringDecoder stringDecoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(1024 * 1024, Delimiters.lineDelimiter()));

        pipeline.addLast(stringDecoder);
        pipeline.addLast(stringEncoder);
        pipeline.addLast(gameLoginHandler);
        pipeline.addLast(simpleChatServerHandler);
    }
}
