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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品：{}", dishDTO);
        dishService.addDishWithFlavor(dishDTO);
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
        dishService.updateDishstatus(dishId,status);
        return Result.success();
    }









}
