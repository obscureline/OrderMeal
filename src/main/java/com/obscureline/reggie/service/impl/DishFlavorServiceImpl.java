package com.obscureline.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obscureline.reggie.entity.DishFlavor;
import com.obscureline.reggie.mapper.DishFlavorMapper;
import com.obscureline.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}