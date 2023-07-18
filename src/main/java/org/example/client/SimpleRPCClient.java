package org.example.client;

import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;
    @Override
    public RPCResponse sendRequest(RPCRequest rpcRequest) {
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
