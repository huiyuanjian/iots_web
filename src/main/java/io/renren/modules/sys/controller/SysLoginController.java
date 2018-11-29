/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.gson.JsonObject;
import io.renren.common.utils.R;
import io.renren.common.utils.TokenUtils;
import io.renren.modules.sys.entity.CacheUser;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;

	@Value("${token}")
	private String TOKEN;

	@Value("${login_cookie_max_age}")
	private String LOGIN_COOKIE_MAX_AGE;

	@Value("${login_cookie_path}")
	private String LOGIN_COOKIE_PATH;

	@Value("${login_cookie_domain}")
	private String LOGIN_COOKIE_DOMAIN;

	@Autowired
	private RedisTemplate redisTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(HttpServletResponse response, String username, String password, String captcha) {
//		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//		if(!captcha.equalsIgnoreCase(kaptcha)){
//			return R.error("验证码不正确");
//		}
		
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			SysUserEntity sysUserEntity = (SysUserEntity)SecurityUtils.getSubject().getPrincipal();
			Long userId = sysUserEntity.getUserId();
			String name = sysUserEntity.getUsername();
			String realName = sysUserEntity.getRealName();
			String salt = sysUserEntity.getSalt();
			//生成每个用户独有的token,响应到前端 -- 主要作用于前后端分离
			String randomToken = TokenUtils.getRandomToken();
			Cookie loginCookie = new Cookie(TOKEN,randomToken);
			try {
				redisTemplate.opsForValue().set(TOKEN + randomToken, objectMapper.writeValueAsString(new CacheUser(userId,name,realName,salt)),Long.valueOf(LOGIN_COOKIE_MAX_AGE.trim()));
			} catch (Exception e) {
				return R.error("账号或密码不正确");
			}
			loginCookie.setMaxAge(Integer.valueOf(LOGIN_COOKIE_MAX_AGE));
			loginCookie.setPath(LOGIN_COOKIE_PATH);
			loginCookie.setHttpOnly(true);
			response.addCookie(loginCookie);
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
	    
		return R.ok();
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
}
