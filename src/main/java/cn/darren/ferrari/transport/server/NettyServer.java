package cn.darren.ferrari.transport.server;

import cn.darren.ferrari.transport.property.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.BeanFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author chensy
 * @date 2019/12/20 15:12
 */
@Slf4j
public class NettyServer {

    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;
    private ChannelInitializer<SocketChannel> channelInitializer;

    @SuppressWarnings("unchecked")
    public NettyServer(NettyServerProperties properties, BeanFactory beanFactory) {
        this.port = properties.getPort();
        bossGroup = new NioEventLoopGroup(properties.getBossNumber());
        workerGroup = new NioEventLoopGroup(properties.getWorkerNumber());
        String beanName = properties.getChannelInitializerBean();
        if (Strings.isNotEmpty(beanName)) {
            if (beanFactory.containsBean(beanName)) {
                channelInitializer = (ChannelInitializer<SocketChannel>) beanFactory.getBean(beanName);
            } else {
                throw new IllegalArgumentException(String.format("could not found channelInitializerBean[%s]",
                        properties.getChannelInitializerBean()));
            }
        } else {
            channelInitializer = (ChannelInitializer<SocketChannel>) beanFactory.getBean("defaultChannelInitializer");
        }
    }


    @PostConstruct
    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        channelFuture = b.bind(port);
        log.info("NettyServer listening on:{}", port);
    }

    @PreDestroy
    public void shutdown() {
        log.info("shutting down NettyServer gracefully...");
        channelFuture.channel().closeFuture();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("shut down NettyServer completely...");
    }
}
