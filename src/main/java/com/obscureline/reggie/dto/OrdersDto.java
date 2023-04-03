package com.obscureline.reggie.dto;

import com.obscureline.reggie.entity.OrderDetail;
import com.obscureline.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

    private int sumNum;
}