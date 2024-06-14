package cn.neusoft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_products")
public class OrderProducts extends Model<OrderProducts> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 订单id 
      */
    private Long orderId;

    /**
      * 商品id 
      */
    private Long goodId;

    /**
      * 数量 
      */
    private Integer count;

    /**
      * 商品规格 
      */
    private String standard;


}