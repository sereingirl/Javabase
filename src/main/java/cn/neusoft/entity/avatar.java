package cn.neusoft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class avatar {
    @TableId(type = IdType.AUTO)
    private int id;
    private String type;
    private long size;
    private String url;
    private String md5;

    public avatar(String type, long size, String url, String md5) {
        this.type = type;
        this.size = size;
        this.url = url;
        this.md5 = md5;
    }
}
