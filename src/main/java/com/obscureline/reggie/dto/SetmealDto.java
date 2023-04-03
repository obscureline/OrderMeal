package com.obscureline.reggie.dto;

import com.obscureline.reggie.entity.Setmeal;
import com.obscureline.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}