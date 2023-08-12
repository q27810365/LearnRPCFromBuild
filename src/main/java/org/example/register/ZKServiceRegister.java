package org.example.register;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.example.loadbalance.LoadBalance;
import org.example.loadbalance.RandomLoadBalance;
import org.example.loadbalance.RoundLoadBalance;

import java.net.InetSocketAddress;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class ZKServiceRegister implements ServiceRegister{

    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";

    private LoadBalance loadBalance = new RoundLoadBalance();

    public ZKServiceRegister() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(retryPolicy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("Zookeeper 连接成功");
    }
    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + getServiceAddress(serverAddress);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            System.out.println(serviceName + " 该服务已存在！");
        }
    }

    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> addressList = client.getChildren().forPath("/" + serviceName);
            String pickAddress = loadBalance.balance(addressList);
            return parseAddress(pickAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InetSocketAddress parseAddress(String string) {
        String[] split = string.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }


}
