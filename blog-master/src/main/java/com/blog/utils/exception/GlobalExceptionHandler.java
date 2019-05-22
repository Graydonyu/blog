package com.blog.utils.exception;

import com.blog.utils.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yuguidong on 2018/9/21.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        log.error("------------------>捕捉到全局异常", e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("msg", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/error");
        return mav;
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResultMap jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {

        //TODO 错误日志处理
        return ResultMap.error(e.getMessage(), "some data");
    }

}
