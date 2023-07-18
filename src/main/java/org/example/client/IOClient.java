package org.example.client;

import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class IOClient {
    public static RPCResponse sendRequest(String host, int port, RPCRequest request) {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            RPCResponse rpcResponse = (RPCResponse) objectInputStream.readObject();

            return rpcResponse;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }
}
