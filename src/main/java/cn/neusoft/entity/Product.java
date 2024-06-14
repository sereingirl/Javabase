package cn.neusoft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("product")
public class Product extends Model<Product> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Double discount;

    /**
      * 销量 
      */
    private Integer sales;

    /*
    *销售额
    */
    private BigDecimal saleMoney;

    /**
      * 分类id 
      */
    private Long categoryId;

    /**
      * 商品图片 
      */
    private String imgs;
    private String createTime;

    /**
      * 是否推荐：0不推荐，1推荐 
      */
    private Boolean recommend;
//     是否删除
    private Boolean isDelete;
    /**
     * 原价
     */
//    todo 这里声明这个属性不存在于数据库中的对应表，不是Good的字段
//     用于联合查询
    @TableField(exist = false)
    private BigDecimal price;



}