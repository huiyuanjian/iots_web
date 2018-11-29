package io.renren.modules.iots.utils.sys;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import io.renren.modules.logger.utils.RedisUtils;
import io.renren.modules.sys.entity.CacheUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zcy
 * @date 2018/11/2015:53
 */
@Component
public class SysUserInfo {


    @Value("${token}")
    private String TOKEN="token";
    
    /*
     * @Author zcy
     * @Description //TODO 得到用户信息
     * @Date 15:59 2018/11/20
     * @Param 
     * @return 
     **/
    public  CacheUser getNowUser(HttpServletRequest request){
        RedisUtils redisUtils= new RedisUtils("10.10.20.171",6379,"123456");
        Cookie[] cookies = request.getCookies();
        CacheUser cacheUser=null;
        for (Cookie cookie: cookies){
            if (cookie.getName().equals(TOKEN)) {
                String key= TOKEN+cookie.getValue();
                String user = redisUtils.get(key);
                if (user!=null && user!=""){
                    cacheUser= JSON.parseObject(user,CacheUser.class);  
                }
            }
        }
       return cacheUser;
    }

}
