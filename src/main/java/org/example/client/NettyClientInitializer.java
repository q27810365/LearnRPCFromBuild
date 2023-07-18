package org.example.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import org.example.codec.JsonSerializer;
import org.example.codec.MyDecode;
import org.example.codec.MyEncode;
import org.example.server.NettyRPCServerHandler;
import org.example.server.ServiceProvider;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new MyDecode());
        channelPipeline.addLast(new MyEncode(new JsonSerializer()));
        channelPipeline.addLast(new NettyClientHandler());
    }
}
