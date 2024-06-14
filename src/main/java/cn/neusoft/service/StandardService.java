package cn.neusoft.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.neusoft.entity.Standard;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.mapper.StandardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StandardService extends ServiceImpl<StandardMapper, Standard> {

    @Resource
    private StandardMapper standardMapper;

    public boolean delete(Standard standard) {
        QueryWrapper<Standard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",standard.getGoodId());
        queryWrapper.eq("value",standard.getValue());
        return remove(queryWrapper);
    }


    public void deleteAll(int GoodId) {
        QueryWrapper<Standard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",GoodId);
        remove(queryWrapper);
    }
}
