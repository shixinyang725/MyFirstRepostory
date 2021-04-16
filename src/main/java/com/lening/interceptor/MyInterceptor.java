package com.lening.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: MyInterceptor  拦截器
 * @Author: PanJunShuang
 * @Date: 2021/4/14 15:47
 * @Version: V1.0
 **/
public class MyInterceptor implements HandlerInterceptor {

    private List exceptUrls;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        //uri和url有什么区别
        if(exceptUrls.contains(uri)){
            //直接放行
            return true;
        }else{
            //需要过滤的，首先要判断他有没有登录，要是没有登录回去登录
            Object ub = request.getSession().getAttribute("ub");
            if(ub==null){
                //他不是特殊的，也没有登录，直接让去登录就OK啦
                request.getRequestDispatcher("/index.html").forward(request, response);
            }else{
                //表示登陆了，但是访问的是不是自己的url，需要判断一下
                Set<String> urls = (Set<String>)request.getSession().getAttribute("urls");
                if(urls!=null){
                    return true;
                }else{
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
                    out.println("<HTML>");
                    out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
                    out.println("  <BODY>");
                    out.println("<script>alert('哥们做人老实不能非法访问，小心有人请你喝茶');</script>");
                    out.println("  </BODY>");
                    out.println("</HTML>");
                    out.flush();
                    out.close();
                }
            }
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public List getExceptUrls() {
        return exceptUrls;
    }

    public void setExceptUrls(List exceptUrls) {
        this.exceptUrls = exceptUrls;
    }
}
