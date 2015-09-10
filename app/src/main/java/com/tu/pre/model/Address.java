package com.tu.pre.model;

import java.util.Map;

/**
 * Created by atlas on 15/9/8.
 * Email:atlas.tufei@gmail.com
 */
public class Address {
    public String address;
    public Map<Double,Double> point;
    public Address(String address, Map<Double, Double> point) {
        this.address = address;
        this.point = point;
    }

    public Address() {
    }
}
