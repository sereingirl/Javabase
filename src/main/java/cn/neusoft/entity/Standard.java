package cn.neusoft.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("good_standard")
public class Standard extends Model<Standard> {

    /**
      * 商品id 
      */
    private Integer goodId;

    /**
      * 商品规格 
      */
    private String value;

    /**
      * 该规格的价格 
      */
    private BigDecimal price;

    /**
      * 该规格的库存 
      */
    private Integer store;


}