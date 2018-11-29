package io.renren.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.CacheUser;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检测用户登陆拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${token}")
    private String TOKEN;

    @Override
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        String requestURI = request.getServletPath();
        Cookie[] cookies = request.getCookies();
        if (cookies == null || ! (cookies.length > 0)){
            response.getWriter().write(objectMapper.writeValueAsString(R.error("用户未登录, 请登陆")));
            return false;
        }
        // 做好常规性非空校验
        Cookie loginCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                loginCookie = cookie;
            }
        }
        String loginToken = null;
        if (loginCookie != null){
            loginToken = loginCookie.getValue();
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(R.error("用户未登录, 请登陆")));
            return false;
        }
        if (loginToken != null) {
            String user = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
            if (user == null || "".equals(user)){
                response.getWriter().write(objectMapper.writeValueAsString(R.error("用户未登录, 请登陆")));
                return false;
            }
            return true;
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(R.error("用户未登录, 请登陆")));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
