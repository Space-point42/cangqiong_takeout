package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;


    /**
     * 实现套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询的信息为{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("要添加的菜品信息为{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        log.info("要查询的套餐id为{}",id);
        SetmealVO setmealVO = setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐信息和对应的菜品信息
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("需要修改的套餐信息为{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteSetmealById(@RequestParam List<Long> ids) {
        log.info("要删除的套餐的id为{}",ids);
        setmealService.deleteSetmealById(ids);
        return Result.success();
    }


    @PostMapping("/status/{status}")
    public Result startOrStopSetmeal(@PathVariable Integer status, Long id) {
        log.info("要改变售卖状态的套餐的id为{}，售卖状态为{}",id,status);
        setmealService.startOrStopSetmeal(id,status);
        return Result.success();

    }



}
