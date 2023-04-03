package com.obscureline.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obscureline.reggie.dto.DishDto;
import com.obscureline.reggie.entity.Dish;
import com.obscureline.reggie.entity.DishFlavor;
import com.obscureline.reggie.mapper.DishMapper;
import com.obscureline.reggie.service.CategoryService;
import com.obscureline.reggie.service.DishFlavorService;
import com.obscureline.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional  //涉及多张表加入事务控制
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品dish中
        this.save(dishDto);

        //菜品加id
            //获取菜品id  dish
            Long dishId = dishDto.getId();
            //for循环添加id
            List<DishFlavor> flavors = dishDto.getFlavors();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            //方式二stream流
            // flavors = flavors.stream().map((item) -> {
            //     item.setDishId(dishId);
            //     return item;
            // }).collect(Collectors.toList());


        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }


    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * 更新
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 起售和停售
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public void setStatus(String[] id, int status) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
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
     * 删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteMore(List<Long> ids) {
        //条件构造器
//        LambdaQueryWrapper<Dish> DqueryWrapper = new LambdaQueryWrapper<>();
        this.removeByIds(ids);


        LambdaQueryWrapper<DishFlavor> DFqueryWrapper = new LambdaQueryWrapper<>();
        DFqueryWrapper.in(DishFlavor::getDishId , ids);
        dishFlavorService.remove(DFqueryWrapper);

//        for (String s : id) {
//            DqueryWrapper.eq(Dish::getId , Long.valueOf(s));
//            DFqueryWrapper.eq(DishFlavor::getDishId , Long.valueOf(s));
//            this.remove(DqueryWrapper);
//            dishFlavorService.remove(DFqueryWrapper);
//            DqueryWrapper.clear();
//            DFqueryWrapper.clear();
//        }
    }
}