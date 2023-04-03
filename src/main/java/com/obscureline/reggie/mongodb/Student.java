package com.obscureline.reggie.mongodb;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Builder
@Data
public class Student {

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 年级
     */
    private String grade;

    /**
     * 班级
     */
    private String classroom;
}