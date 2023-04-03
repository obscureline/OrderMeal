package com.obscureline.reggie.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScProduct implements Serializable {

    private String name;
    private Integer number;
    private Integer id;
    private String productImg;

}