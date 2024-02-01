package com.rd.rockpaperscissorstelnet.configuration;

import com.rd.rockpaperscissorstelnet.domain.Player;
import com.rd.rockpaperscissorstelnet.handler.GameChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.InetSocketAddress;

@Configuration
@RequiredArgsConstructor
public class NettyConfiguration {
    private final NettyProperties nettyProperties;

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public AttributeKey<Player> userAttributeKey() {
        return AttributeKey.newInstance("USER");
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup boosGroup() {
        return new NioEventLoopGroup(nettyProperties.getBossGroupSize());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorkerGroupSize());
    }

    @Bean
    public ServerBootstrap serverBootstrap(GameChannelInitializer gameChannelInitializer) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(gameChannelInitializer);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
        return serverBootstrap;
    }

    @Bean
    public InetSocketAddress tcpSocketAddress() {
        return new InetSocketAddress(nettyProperties.getPort());
    }

    @Bean
    public StringEncoder stringEncoder(){
        return new StringEncoder();
    }

    @Bean
    public StringDecoder stringDecoder(){
        return new StringDecoder();
    }
}
