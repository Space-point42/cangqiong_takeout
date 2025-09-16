package com.sky.controller.admin;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("AdminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置店铺的状态：{}", status==1 ? "营业中":"已打烊");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的状态为：{}",shopStatus==1 ? "营业中":"已打烊");
        return Result.success(shopStatus);
    }

}
