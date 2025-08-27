package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品id查询对应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> selectByIds(List<Long> dishIds);

    /**
     * 插入套餐对应的菜品信息
     * @param setmealDishes
     */
    void insert(List<SetmealDish> setmealDishes);

    /**
     * 所有的查询操作都可用这个方法
     * @param setmealDish
     * @return
     */
    List<SetmealDish> select(SetmealDish setmealDish);

    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
