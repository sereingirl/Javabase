package cn.neusoft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor

/*
商品规格及对应库存
 */
public class GoodStandard {
    private int goodId;
    private String value;
    private double price;
    private int store;




}
