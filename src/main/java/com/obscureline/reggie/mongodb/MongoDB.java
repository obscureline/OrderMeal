package com.obscureline.reggie.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/test")
public class MongoDB {

    @Autowired
    private MongoTemplate mongoTemplate;

    //插入
    @RequestMapping("/2")
    public void insert(){
        //        Student student = Student.builder().name("张三").age(10).grade("一年级").classroom("1班").build();
        //        mongoTemplate.insert(student);
        Student build = Student.builder().name("jiangke").age(12).grade("大学").classroom("五班").build();
        mongoTemplate.insert(build);
    }

    //删除
    @RequestMapping("/4")
    public void delete(){
        //        Query query = new Query();
        //        query.addCriteria(Criteria.where("age").is(11));
        //        mongoTemplate.remove(query,Student.class);
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("jiangke"));
        mongoTemplate.remove(query,Student.class);
    }

    //更新
    @RequestMapping("/3")
    public void update(){
        //        Query query = new Query();
        //        query.addCriteria(Criteria.where("grade").is("一年级"));
        //        mongoTemplate.updateFirst(query, Update.update("age",11),Student.class);
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("jiangke"));
        mongoTemplate.updateFirst(query,Update.update("name","jianghao"),Student.class);
    }

    //查询
    @RequestMapping("/5")
    public void find(){
        //        Query query = new Query();
        //        query.addCriteria(Criteria.where("grade").is("一年级"));
        //        List<Student> students = mongoTemplate.find(query, Student.class);
        //        students.forEach(student -> {
        //            System.out.println(student.toString());
        //        });
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("jianghao"));
        List<Student> students = mongoTemplate.find(query, Student.class);
        students.forEach(student -> {
            System.out.println(student.toString());
        });
    }
}