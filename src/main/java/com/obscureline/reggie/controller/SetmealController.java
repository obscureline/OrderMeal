package com.obscureline.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.obscureline.reggie.common.R;
import com.obscureline.reggie.dto.SetmealDto;
import com.obscureline.reggie.entity.Category;
import com.obscureline.reggie.entity.Setmeal;
import com.obscureline.reggie.service.CategoryService;
import com.obscureline.reggie.service.SetmealDishService;
import com.obscureline.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RequestMapping("/setmeal")
@RestController
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}" , setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //构造分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPageInfo = new Page<>(page , pageSize);

        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Setmeal::getName , name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //执行分页查询
        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝  将一拷贝到二  排除records
        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");
        List<Setmeal> records = pageInfo.getRecords();

        //利用stream流将Setmeal转成SetmealDto型 从而展示SetmealDto里的数据   即口味类型和分类（categoryName）
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPageInfo.setRecords(list);
        return R.success(dtoPageInfo);
    }

    /**
     * 修改菜品
     * @param setmealDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.updateWithsetmealDishes(setmealDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithsetmealDishes(id);
        return R.success(setmealDto);
    }

    /**
     * 起售和停售
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> set(String ids ,@PathVariable int status){
        String[] id = ids.split(",");
        setmealService.setStatus(id,status);
        return R.success("状态修改成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache" , allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteMore(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 套餐商品展示
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache" , key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list( Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null , Setmeal::getCategoryId , setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null , Setmeal::getStatus , setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}