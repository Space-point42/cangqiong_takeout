package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /***
     * 添加菜品和对应的口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void addDishWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //添加一道菜品，并在mapper的xml文件中配置返回插入的菜品的id
        dishMapper.insert(dish);

        //添加对应的菜品的口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()) {
            for(DishFlavor f : flavors) {
                f.setDishId(dish.getId());
                //批量添加菜品口味
                dishFlavorMapper.batchInsert(flavors);
            }

        }






    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        //检测要删除的菜品是否在套餐中，在就不能删除
        List<Long> list = setmealDishMapper.selectByIds(ids);
        if(list != null && !list.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //检测要删除的菜品是否在起售状态中，在就不能删除
        List<Long> onSaleDish = dishMapper.selectByIdsWithStatus(ids);
        if(onSaleDish != null && !onSaleDish.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //删除菜品和它对应的口味
        for(Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    @Transactional
    public DishVO getByIdWithFlavor(Long dishId) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.getDishByDishId(dishId);
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(dishId);
        dishVO.setFlavors(flavors);
        return dishVO;


    }

    @AutoFill(OperationType.UPDATE)
    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        dishMapper.update(dishDTO);

        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()) {
            for(DishFlavor f : flavors) {
                f.setDishId(dishDTO.getId());
                //批量添加菜品口味
                dishFlavorMapper.batchInsert(flavors);
            }

        }

    }

    @Override
    public void updateDishstatus(Long dishId, Integer status) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setId(dishId);
        dishDTO.setStatus(status);
        dishMapper.update(dishDTO);

    }

    @Override
    public List<DishVO> getByCategoryId(Long categoryId) {
        return dishMapper.select(categoryId);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<DishVO> dishvoList = dishMapper.select(dish.getCategoryId());



        for (DishVO d : dishvoList) {


            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(d.getId());

            d.setFlavors(flavors);

        }

        return dishvoList;
    }



}
