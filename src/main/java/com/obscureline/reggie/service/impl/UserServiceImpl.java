package com.obscureline.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obscureline.reggie.entity.User;
import com.obscureline.reggie.mapper.UserMapper;
import com.obscureline.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}