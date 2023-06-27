package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.AuthException;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 判断异常类型
         * 如果是未登陆异常，执行相关操作
         */
        if (ex instanceof NoLoginException){
            //如果捕获的是未登录异常，重定向到登陆页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        //设置默认异常处理
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code",400);
        modelAndView.addObject("msg","系统异常，请稍后重试");

        //判断handlerMethod
        if (handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断注解是否存在（如果不存在，表示返回的是视图，如果存在表示返回的是json对象）
            if (null==responseBody){
                //返回视图
                //判断异常类型
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());

                }else  if (ex instanceof AuthException){//权限认证异常
                    AuthException a = (AuthException) ex;
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());

                }
                return modelAndView;
            }else {
                //返回json对象
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请稍后重试！");
                //如果捕获的是自定义异常
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if (ex instanceof AuthException){//权限认证异常
                    AuthException a = (AuthException) ex;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }
                    //设置响应类型，和编码格式（响应json格式）
                response.setContentType("application/json;charset=utf-8");
                //得到输出流
                PrintWriter printWriter=null;
                try {
                   printWriter = response.getWriter();
                    //将对象转为json格式，通过输出流输出
                    printWriter.write(JSON.toJSONString(resultInfo));
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (printWriter !=null){
                        printWriter.close();
                    }
                }
                return null;
            }
        }


        return modelAndView;
    }
}
