package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserLogInMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserLogInService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLogInServiceImpl implements UserLogInService {

    private final static String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserLogInMapper userLogInMapper;

    @Override
    public User logIn(UserLoginDTO userLoginDTO) {
        //获取用户的微信openId
        String openId = getOpenId(userLoginDTO);
        //判断openId是否为空
        if(openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userLogInMapper.select(openId);

        //判断是否为新用户

        if(user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userLogInMapper.insert(user);
        }
        return user;
    }


    /**
     * 获取openId
     * @param userLoginDTO
     * @return
     */
    private String getOpenId(UserLoginDTO userLoginDTO) {
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("grant_type","authorization_code");
        map.put("js_code",userLoginDTO.getCode());

        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openId = jsonObject.getString("openid");
        return openId;
    }
}
