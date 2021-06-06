package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @ControllerAdvice表示当前类是一个基于注解的异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        // 重定向视图时，只有简单类型的get请求参数才能被写入浏览器地址栏
        String viewName = "admin-add";
        return commonResolveException(exception, request, response,viewName);
    }

    @ExceptionHandler(AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolveException(exception, request, response,viewName);
    }

    // @ExceptionHandler表示将一个具体的异常类型和当前方法关联起来
    // value属性是一个Class[]数组，当数组的元素只有一个时，属性名value可以不写
    @ExceptionHandler(LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        // 指定异常处理完成后要去的页面
        String viewName = "admin-login";
        return commonResolveException(exception,request,response,viewName);
    }

    // 当异常类型为Exception时，value属性可以不配置
    @ExceptionHandler
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        // 指定异常处理完成后要去的页面
        String viewName = "system-error";
        return commonResolveException(exception, request, response, viewName);
    }

    /**
     * 核心异常处理方法
     * @param exception SpringMVC 捕获到的异常对象
     * @param request 为了判断当前请求是“普通请求”还是“Ajax请求”需要传入原生request对象
     * @param response 为了能够将JSON字符串作为当前请求的响应数据返回给浏览器
     * @param viewName 指定要前往的视图名称
     * @return ModelAndView
     * @throws
     */
    private ModelAndView commonResolveException(Exception exception,
                                                HttpServletRequest request,
                                                HttpServletResponse response,
                                                String viewName) throws IOException {
        // 1、判断当前请求是普通请求还是ajax请求
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        // 2、如果是ajax请求
        if (judgeResult){
            // 3、从当前异常对象中获取异常信息
            String message = exception.getMessage();
            // 4、创建ResultEntity
            ResultEntity<Object> resultEntity = ResultEntity.failed(message);
            // 5、创建Gson对象
            Gson gson = new Gson();
            // 6、将ResultEntity转换成json字符串
            String json = gson.toJson(resultEntity);
            // 7、将当前json字符串作为当前请求的响应体数据返回给浏览器
            /*
                这里要设置后端返回的数据的contentType为application/json;charset=utf-8，
                原因是虽然项目中设置了字符过滤器，过滤了响应体的中文乱码，但是打印发现响应对象response的属性ContentType的值为null，
                导致{"result":"FAILED","message":"ç™»å½•å¤±è´¥ï¼Œè¯·ç¡®è®¤è´¦å·å¯†ç æ˜¯å¦æ­£ç¡®ï¼"}，
                虽然alert(result.message)的最终结果没有中文乱码，但是浏览器的network中还是有如上乱码的，所以还是设置一下比较好
            */
            System.out.println(response.getContentType());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(json);
            // 8、返回null，不给SpringMVC提供ModelAndView对象，这样SpringMVC就知道不需要框架解析视图来提供响应，而是程序员自己提供了响应
            return null;
        }
        // 9、创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        // 10、将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
        // 11、设置目标视图名称
        modelAndView.setViewName(viewName);
        // 12、返回ModelAndView对象
        return modelAndView;
    }
}
