package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageSelect(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }


    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        //在setmeal表中添加对应套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //在setmeal_dish表中添加信息
        //获取菜品所对应的套餐的id
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insert(setmealDishes);
    }

    @Transactional
    @Override
    public SetmealVO getSetmealById(Long id) {
        //获取套餐相关信息
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal = setmealMapper.select(setmeal);
        BeanUtils.copyProperties(setmeal,setmealVO);

        //获取套餐相关的菜品信息
        SetmealDish setmealDish = new SetmealDish();
        setmealDish.setSetmealId(id);
        List<SetmealDish> setmealDishList = setmealDishMapper.select(setmealDish);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    @Transactional
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //修改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //修改对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //先删除套餐原来的菜品信息
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        //再添加套餐对应的新的菜品信息
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insert(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteSetmealById(List<Long> ids) {
        //先删除对应id的套餐
        setmealMapper.deleteSetmealById(ids);
        //再删除套餐所对应的菜品
        for (Long id : ids) {
            setmealDishMapper.deleteBySetmealId(id);
        }

    }

    @Override
    public void startOrStopSetmeal(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.startOrStopSetmeal(setmeal);
    }
}
