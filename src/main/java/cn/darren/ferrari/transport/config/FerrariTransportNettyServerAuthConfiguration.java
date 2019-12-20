package cn.darren.ferrari.transport.config;

import cn.darren.ferrari.transport.property.NettyServerProperties;
import cn.darren.ferrari.transport.server.NettyServer;
import cn.darren.ferrari.transport.server.handler.EchoHanlder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chensy
 * @date 2019/12/20 15:02
 */
@Configuration
@EnableConfigurationProperties(NettyServerProperties.class)
@ConditionalOnProperty(prefix = "transport", name = "enabled", havingValue = "true")
public class FerrariTransportNettyServerAuthConfiguration {

    @Bean
    @ConditionalOnMissingBean(ChannelInitializer.class)
    ChannelInitializer<SocketChannel> defaultChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                ch.pipeline().addLast(new LengthFieldPrepender(4));
                ch.pipeline().addLast(new EchoHanlder());
            }
        };
    }

    @Bean
    NettyServer nettyServer(NettyServerProperties nettyServerProperties, BeanFactory beanFactory) {
        return new NettyServer(nettyServerProperties, beanFactory);
    }
}
