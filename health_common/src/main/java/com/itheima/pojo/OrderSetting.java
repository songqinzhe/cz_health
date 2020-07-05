package com.itheima.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 预约设置
 */
public class OrderSetting implements Serializable{
    private Integer id ;
    private Date orderDate;//预约设置日期  2020-05-23 00:00:00
    private int number;//可预约人数
    private int reservations ;//已预约人数
    private int date;

    public int getDate() {
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        String day = dd.format(orderDate);
        return Integer.parseInt(day);
    }

    public void setDate(int date) {
        this.date = date;
    }

    public OrderSetting() {
    }

    public OrderSetting(Date orderDate, int number) {
        this.orderDate = orderDate;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }
}
