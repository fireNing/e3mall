package cn.hpn.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);


    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler,
                                         Exception e) {

        //写日志；
        logger.error("系统异常",e);
        //发邮件，短信通知;
        //展示错误页面;
        ModelAndView model = new ModelAndView();
        model.addObject("message","系统发生异常，请稍后重试");
        model.setViewName("error/exception");
        return model;
    }
}
