package org.example.client;

import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;
import org.example.register.ServiceRegister;
import org.example.register.ZKServiceRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;
    private ServiceRegister serviceRegister;

    public SimpleRPCClient() {
        this.serviceRegister = new ZKServiceRegister();
    }
    @Override
    public RPCResponse sendRequest(RPCRequest rpcRequest) {

        InetSocketAddress address = serviceRegister.serviceDiscovery(rpcRequest.getInterfaceName());
        host = address.getHostName();
        port = address.getPort();

        try {
            Socket socket = new Socket(host,port);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(rpcRequest);
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            RPCResponse rpcResponse = (RPCResponse) objectInputStream.readObject();

            return rpcResponse;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }

    }
}
