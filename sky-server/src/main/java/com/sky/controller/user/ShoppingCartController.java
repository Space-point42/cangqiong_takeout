package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车中添加的信息为 {}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> getAll() {
        log.info("获取购物车中的信息");
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAll();
        return Result.success(shoppingCarts);
    }

    @DeleteMapping("/clean")
    public Result deleteAll(){
        log.info("清空购物车");
        shoppingCartService.deleteAll();
        return Result.success();
    }

    /**
     * 减少菜品数量
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("要减少的菜品信息为 {}", shoppingCartDTO);
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }

}
