package com.blog;

import com.blog.utils.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogMasterApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test(){
        Set<String> scan = redisUtil.scan("rank_post_*");
        if(CollectionUtils.isNotEmpty(scan)){
            scan.forEach(p -> {
                System.out.println(p);
                Object hget = redisUtil.hget(p, "post:title");
                System.out.println("title=" + hget.toString());
            });
        }
    }

}
