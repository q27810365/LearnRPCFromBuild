package org.example.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        RPCResponse rpcResponse = getResponse(rpcRequest);
        channelHandlerContext.writeAndFlush(rpcResponse);
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(RPCRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;

        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
            Object invoke = method.invoke(service, rpcRequest.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RPCResponse.fail();
        }
    }
}
