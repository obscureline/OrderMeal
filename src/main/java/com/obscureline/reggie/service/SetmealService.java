package com.obscureline.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.obscureline.reggie.dto.SetmealDto;
import com.obscureline.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐  保存套餐与菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    //根据id查询菜品信息和对应的套餐信息
    public SetmealDto getByIdWithsetmealDishes(Long id);

    //更新菜品信息，同时更新对应的套餐信息
    public void updateWithsetmealDishes(SetmealDto setmealDto);

    //起售和停售
    public void setStatus(String[] id,int status);


    //删除
    public void deleteMore(List<Long> ids);
}
