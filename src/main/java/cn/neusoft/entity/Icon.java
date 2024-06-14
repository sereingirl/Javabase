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

import java.util.List;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="icon")
public class Icon extends Model<Icon> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 图标的标识码 
      */
    private String value;
    //    todo 这里声明这个属性不存在于数据库中的对应表，不是Good的字段
//     用于联合查询
    @TableField(exist = false)
    private List<Category> categories;

}