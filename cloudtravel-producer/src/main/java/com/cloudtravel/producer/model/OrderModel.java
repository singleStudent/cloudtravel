package com.cloudtravel.producer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderModel implements Serializable , Cloneable{

    private String orderId;

    private String orderDesc;

    @Override
    public OrderModel clone() throws CloneNotSupportedException {
        return (OrderModel)super.clone();
    }
}
