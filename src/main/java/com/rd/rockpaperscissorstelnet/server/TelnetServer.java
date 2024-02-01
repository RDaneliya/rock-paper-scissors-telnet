package com.rd.rockpaperscissorstelnet.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelnetServer {
    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress tcpPort;
    private Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        ChannelFuture channelFuture = serverBootstrap.bind(tcpPort)
                                                     .sync();
        log.info("Server is started. Listening port {}", tcpPort.getPort());
        channel = channelFuture.channel()
                               .closeFuture()
                               .sync()
                               .channel();
    }

    @PreDestroy
    public void stop() {
        if(channel != null){
            channel.close();
            channel.parent().close();
        }
    }
}
