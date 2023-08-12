package org.example.register;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void register(String serviceName, InetSocketAddress inetSocketAddress);
    InetSocketAddress serviceDiscovery(String serviceName);
}
