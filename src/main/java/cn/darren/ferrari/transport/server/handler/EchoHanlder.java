package cn.darren.ferrari.transport.server.handler;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author chensy
 * @date 2019/12/20 16:10
 */
@Slf4j
public class EchoHanlder extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Charset CHARSET = Charsets.UTF_8;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.info("receive msg:{}", new String(bytes, CHARSET));
        ByteBuf resBuf = ctx.alloc().buffer();
        resBuf.writeBytes(bytes);
        ctx.writeAndFlush(resBuf);
    }
}
