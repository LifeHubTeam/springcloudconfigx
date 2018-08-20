package cn.lifehub.config.controller;

import cn.lifehub.config.constants.RedisConstant;
import cn.lifehub.config.models.json.AuthUserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RedisTemplate<Object, Object> redisTemplate;

    public HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public String getToken(){
        return this.getRequest().getHeader("token");
    }

    public AuthUserJson getAuthUser(){
        return (AuthUserJson) redisTemplate.opsForValue().get(RedisConstant.CONFIG_AUTH_USER_KEY + this.getToken());
    }
}
