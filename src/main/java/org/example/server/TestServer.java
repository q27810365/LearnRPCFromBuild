package org.example.server;

import org.example.common.Blog;
import org.example.service.BlogService;

import java.util.HashMap;
import java.util.Map;

public class TestServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        BlogServiceImpl blogService = new BlogServiceImpl();
//        Map<String, Object> serviceProvider = new HashMap<>();
//        serviceProvider.put("com.example.service.UserService", userService);
//        serviceProvider.put("com.example.service.BlogService", blogService);
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(8899);
    }
}
