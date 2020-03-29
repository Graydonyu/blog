package com.blog.search.controller;

import com.blog.common.resultVO.R;
import com.blog.search.dto.PostDTO;
import com.blog.search.feign.BlogMasterClient;
import com.blog.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hw-search/admin")
public class AdminController {

    @Autowired
    BlogMasterClient blogMasterClient;

    @Autowired
    SearchService searchService;


    /**
     * 初始化构建数据库所有数据
     * 需要在admin后台管理系统添加初始化按钮
     * @return
     */
    @RequestMapping("/initEsIndex")
    public R initEsIndex() {

        int total = 0;

        int size = 10000;
        for(int i = 1; i < 1000; i ++) {

            R<List<PostDTO>> results = blogMasterClient.findPostDTOByPage(i, size);
            if (!results.ok()) {
                return R.failed("构建失败！！");
            }

            int num = searchService.initEsIndex(results.getData());

            total += num;
            if(num < size) {
                break;
            }
        }
        return R.ok("ES索引库初始化成功！共" + total + "条记录");
    }

}
