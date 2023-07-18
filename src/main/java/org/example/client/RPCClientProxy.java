package org.example.client;

import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {

    private RPCClient rpcClient;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest rpcRequest = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes()).build();

        RPCResponse rpcResponse = rpcClient.sendRequest(rpcRequest);

        return rpcResponse.getData();
    }

    <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
