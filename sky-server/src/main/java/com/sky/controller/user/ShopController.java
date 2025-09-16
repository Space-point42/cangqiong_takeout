package com.sky.controller.user;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("UserShopController")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的状态为：{}",shopStatus==1 ? "营业中":"已打烊");
        return Result.success(shopStatus);
    }

}
