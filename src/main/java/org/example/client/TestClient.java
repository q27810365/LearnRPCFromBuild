package org.example.client;

import org.example.common.Blog;
import org.example.common.User;
import org.example.service.BlogService;
import org.example.service.UserService;

public class TestClient {
    public static void main(String[] args) {
        RPCClient rpcClient = new NettyRPCClient("127.0.0.1", 8877);
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient);
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        User userByUserId = userService.getUserByUserId(10);
        System.out.println("从服务端得到的User是：" + userByUserId);

        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);

        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);

        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);
    }
}
