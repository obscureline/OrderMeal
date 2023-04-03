package com.obscureline.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obscureline.reggie.entity.ShoppingCart;
import com.obscureline.reggie.mapper.ShoppingCartMapper;
import com.obscureline.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}