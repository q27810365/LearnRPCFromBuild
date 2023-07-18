package org.example.server;

import org.example.common.Blog;
import org.example.service.BlogService;

public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").userId(22).build();
        System.out.println("客户查询了" + id + "博客");
        return blog;
    }
}
