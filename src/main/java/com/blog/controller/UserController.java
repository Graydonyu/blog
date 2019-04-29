package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.shiro.AccountProfile;
import com.blog.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("/center")
    public String center(@RequestParam(defaultValue = "1") Integer current,
                         @RequestParam(defaultValue = "10")Integer size){
        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        log.info("-------------->进入个人中心");

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId()).orderByDesc("created");

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        req.setAttribute("pageData", pageData);

        return "user/center";
    }

    @GetMapping("/collection")
    public String collection( @RequestParam(defaultValue = "1") Integer current,
                              @RequestParam(defaultValue = "10")Integer size) {

        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        req.setAttribute("pageData", new Page());

        return "user/collection";
    }

    /**
     * 头像上传
     *
     * Constant.uploadDir是要上传的路径
     * Constant.uploadUrl是我另一个tomcat的项目路径
     * Constant.uploadDir对应的就是这个Constant.uploadUrl的访问路径。
     *
     * 可以通过另外部署一个tomcat或者nginx实现
     * 当然了，上传到云存储服务器更好。
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file) throws Exception {

        if(file.isEmpty()) {
            return R.failed("上传失败");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = Constant.uploadDir;

        fileName = "avatar_" + getProfileId() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }


        file.transferTo(dest);
        log.info("上传成功后的文件路径未：" + filePath + fileName);

        String path = filePath + fileName;
        String url = Constant.uploadUrl + fileName;

        log.info("url ---> {}", url);

        User current = userService.getById(getProfileId());
        current.setAvatar(url);
        userService.updateById(current);

        //更新shiro的信息
        AccountProfile profile = getProfile();
        profile.setAvatar(url);

        return R.ok(url);
    }

    @ResponseBody
    @PostMapping("/message/nums")
    public Object getMessNums() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("count", 2);

        return result;
    }

    @RequestMapping("/{id}")
    public String home(@PathVariable Long id) {

        return "user/home";
    }

    @GetMapping("/setting")
    public String setting() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);

        req.setAttribute("user", user);
        return "user/setting";
    }

    @GetMapping("/message")
    public String message() {
        return "user/message";
    }
}

