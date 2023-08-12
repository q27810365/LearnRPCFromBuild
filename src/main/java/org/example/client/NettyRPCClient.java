package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;
import org.example.register.ServiceRegister;
import org.example.register.ZKServiceRegister;

import java.net.InetSocketAddress;

public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;
    private ServiceRegister serviceRegister;
    public NettyRPCClient() {
        this.serviceRegister = new ZKServiceRegister();
    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    /**
     * Netty 异步特性导致需要获取对应的Response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest rpcRequest) {
        InetSocketAddress address = serviceRegister.serviceDiscovery(rpcRequest.getInterfaceName());
        host = address.getHostName();
        port = address.getPort();

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();

            channel.writeAndFlush(rpcRequest);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            AttributeKey<RPCResponse> attributeKey = AttributeKey.valueOf("RPCResponse");
            RPCResponse rpcResponse = channel.attr(attributeKey).get();
            System.out.println(rpcResponse);
            return rpcResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
