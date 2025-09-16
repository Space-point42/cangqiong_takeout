package com.sky.controller.user;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserLogInService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user/user/login")
@Slf4j
public class UserLogInController {
    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    private UserLogInService userLogInService;

    @PostMapping
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("授权微信登录的code：{}", userLoginDTO.getCode());

        //获取用户的信息
        User user = userLogInService.logIn(userLoginDTO);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(user.getOpenid())
                .build();
        return Result.success(userLoginVO);
    }
}
