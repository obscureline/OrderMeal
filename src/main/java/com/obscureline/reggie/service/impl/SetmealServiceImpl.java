package com.obscureline.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obscureline.reggie.common.CustomException;
import com.obscureline.reggie.dto.SetmealDto;
import com.obscureline.reggie.entity.Setmeal;
import com.obscureline.reggie.entity.SetmealDish;
import com.obscureline.reggie.mapper.SetmealMapper;
import com.obscureline.reggie.service.SetmealDishService;
import com.obscureline.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐  保存套餐与菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }

        //方式二stream流
        //setmealDishes.stream().map((item) -> {
        //    item.setSetmealId(setmealDto.getId());
        //    return item;
        //}).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id查询菜品信息和对应的套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithsetmealDishes(Long id) {
        //查询菜品基本信息，从setmeal表查询
        Setmeal setmeal= this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //查询当前菜品对应的套餐信息，从dish_flavor表查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId , setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /**
     *
     * @param setmealDto
     */
    @Override
    public void updateWithsetmealDishes(@RequestBody SetmealDto setmealDto) {
        //更新dish表基本信息
        this.updateById(setmealDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId , setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 起售和停售
     * @param id
     * @param status
     */
    @Override
    public void setStatus(String[] id, int status) {
        UpdateWrapper<Setmeal> updateWrapper = new UpdateWrapper<>();
        if(status==0) {
            for (String s : id) {
                updateWrapper.eq("id", Long.valueOf(s));
                updateWrapper.set("status",0);
                this.update(updateWrapper);
                updateWrapper.clear();
            }
        }
        else if(status==1){
            for(String s:id){
                updateWrapper.eq("id",Long.valueOf(s));
                updateWrapper.set("status",1);
                this.update(updateWrapper);
                updateWrapper.clear();
            }
        }
    }

    /**
     * 删除套餐及菜品关联
     * @param ids
     */
    @Override
    public void deleteMore(List<Long> ids) {
        //查询套餐状态，确定是否可用删除
       LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       lambdaQueryWrapper.in(Setmeal::getId , ids);
       lambdaQueryWrapper.eq(Setmeal::getStatus , 1);

        int count = this.count(lambdaQueryWrapper);
        if (count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)

        //删除关系表中的数据----setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId , ids);
        setmealDishService.remove(lambdaQueryWrapper1);
    }
}