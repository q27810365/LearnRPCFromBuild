package org.example.server;

import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            RPCResponse rpcResponse = getResponse(rpcRequest);

            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("从IO读取数据错误");
        }
    }

    private RPCResponse getResponse(RPCRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
            Object invoke = method.invoke(service, rpcRequest.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RPCResponse.fail();
        }
    }
}
