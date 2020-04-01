package com.blog.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.entity.UserCollection;
import com.blog.entity.UserMessage;
import com.blog.entity.enums.IsEnum;
import com.blog.shiro.AccountProfile;
import com.blog.utils.Constant;
import com.blog.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
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
                         @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        log.info("-------------->进入个人中心");

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId()).orderByDesc("created");

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().stream().forEach(p -> postService.setViewCount(p, IsEnum.NO));
        }
        req.setAttribute("pageData", pageData);

        return "user/center";
    }

    @GetMapping("/collection")
    public String collection(@RequestParam(defaultValue = "1") Integer current,
                             @RequestParam(defaultValue = "10") Integer size) {

        Page<UserCollection> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = userCollectionService.pageMaps(page, new QueryWrapper<UserCollection>().eq("user_id", getProfileId()).orderByDesc("created"));

        postService.join(pageData, "post_id");

        req.setAttribute("pageData", pageData);

        return "user/collection";
    }

    /**
     * 头像上传
     * <p>
     * Constant.uploadDir是要上传的路径
     * Constant.uploadUrl是我另一个tomcat的项目路径
     * Constant.uploadDir对应的就是这个Constant.uploadUrl的访问路径。
     * <p>
     * 可以通过另外部署一个tomcat或者nginx实现
     * 当然了，上传到云存储服务器更好。
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file,
                    @RequestParam(name = "type", defaultValue = "avatar") String type) throws Exception {

        if (file.isEmpty()) {
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

        if ("avatar".equalsIgnoreCase(type)) {
            fileName = "/avatar/avatar_" + getProfileId() + suffixName;

        } else if ("post".equalsIgnoreCase(type)) {
            fileName = "/post/post_" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + suffixName;
        }

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

        if ("avatar".equalsIgnoreCase(type)) {
            User current = userService.getById(getProfileId());
            current.setAvatar(url);
            userService.updateById(current);

            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setAvatar(url);
        }

        return R.ok(url);
    }

    @ResponseBody
    @PostMapping("/message/nums")
    public Object getMessNums() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("count", 2);

        return result;
    }

    @GetMapping("/{id}")
    public String home(@PathVariable Long id) {

        User user = userService.getById(id);
        user.setPassword(null);

        //30天内的文章
        Date date30Before = DateUtil.offsetDay(new Date(), -30).toJdkDate();
        List<Post> posts = postService.list(new QueryWrapper<Post>()
                .eq("user_id", id)
                .ge("created", date30Before)
                .orderByDesc("created"));

        //30天内的回答
        List<Map<String, Object>> comments = commentService.listMaps(new QueryWrapper<Comment>()
                .eq("user_id", id)
                .ge("created", date30Before)
                .orderByDesc("created")
                .last("limit 5"));
        if(CollectionUtils.isNotEmpty(comments)){
            postService.join(comments,"post_id");

            comments.forEach(comment -> {
                String created = DateUtils.getDateDiff((Date) comment.get("created"));
                comment.put("created",created);
            });
        }

        //TODO 动作记录

        req.setAttribute("user", user);
        req.setAttribute("posts", posts);
        req.setAttribute("comments", comments);

        return "user/home";
    }

    @GetMapping("/setting")
    public String setting() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);

        req.setAttribute("user", user);
        return "user/setting";
    }

    @ResponseBody
    @PostMapping("/setting")
    public R postSetting(User user) {
        User tempUser = userService.getById(getProfileId());
//        tempUser.setEmail(user.getEmail());
        tempUser.setUsername(user.getUsername());
        tempUser.setGender(user.getGender());
        tempUser.setSign(user.getSign());
        tempUser.setMobile(user.getMobile());

        boolean isSucc = userService.updateById(tempUser);
        if (isSucc) {
            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setUsername(user.getUsername());
            profile.setGender(user.getGender());
        }

        return isSucc ? R.ok(user) : R.failed("更新失败");
    }

    @ResponseBody
    @PostMapping("/resetPwd")
    public R resetPwd(String nowpass, String pass) {

        User user = userService.getById(getProfileId());

        String nowPassMd5 = SecureUtil.md5(nowpass);
        if (!nowPassMd5.equals(user.getPassword())) {
            return R.failed("密码不正确");
        }

        user.setPassword(SecureUtil.md5(pass));
        userService.updateById(user);

        return R.ok(null);
    }

    @GetMapping("/message")
    public String message(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size) {
        Page<UserMessage> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = userMessageService.pageMaps(page, new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .orderByDesc("created"));

        userService.join(pageData, "from_user_id");

        //评论你文章的提示
        postService.join(pageData, "post_id");
        //评论你的评论的提示
        commentService.join(pageData, "comment_id");

        req.setAttribute("pageData", pageData);

        return "user/message";
    }

    @ResponseBody
    @PostMapping("/message/remove")
    public R removeMsg(Long id, boolean all) {

        QueryWrapper<UserMessage> warapper = new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .eq(!all, "id", id);

        //只能删除自己的消息
        boolean res = userMessageService.remove(warapper);

        return res ? R.ok(null) : R.failed("删除失败");
    }
}

