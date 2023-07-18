package org.example.server;

import org.example.common.User;
import org.example.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("查询" + id + "用户");
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }

    public Integer insertUserId(User user) {
        System.out.println("插入数据成功" + user);
        return 1;
    }
}
