package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品：{}", dishDTO);
        dishService.addDishWithFlavor(dishDTO);
        //清楚指定缓存
        clearCache("dish_"+dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页展示{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    @DeleteMapping
    public Result deleteDish(@RequestParam List<Long> ids) {
        log.info("要删除的菜品的id为{}",ids);
        //删除所有缓存
        clearCache("dish_*");
        dishService.deleteByIds(ids);
        return Result.success();
    }


    /**
     * 根据id查询菜品和对应的口味
     * @param dishId
     * @return
     */
    @GetMapping("/{dishId}")
    public Result<DishVO> selectDishById(@PathVariable Long dishId) {
        log.info("要查询的菜品id为{}",dishId);
        DishVO dishVO = dishService.getByIdWithFlavor(dishId);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品信息和对应的口味
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("插入的菜品为{}", dishDTO);
        clearCache("dish_*");
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 改变菜品的售卖状态
     * @param dishId
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@RequestParam("id") Long dishId, @PathVariable Integer status ) {
        log.info("菜品id为{}的状态改为{}",dishId,status);
        clearCache("dish_*");
        dishService.updateDishstatus(dishId,status);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<DishVO>> getByCategoryId(@RequestParam("categoryId") Long categoryId) {
        log.info("要查询的分类id为{}",categoryId);
        List<DishVO> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }


    /**
     * 清除指定缓存数据
     * @param pattern
     */
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }





}
