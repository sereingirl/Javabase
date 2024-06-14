package cn.neusoft.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.neusoft.entity.Address;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {

    @Resource
    private AddressMapper addressMapper;

    public List<Address> findAllById(Long id) {
        QueryWrapper<Address> listQueryWrapper = new QueryWrapper<>();
        listQueryWrapper.eq("user_id",id);
        return list(listQueryWrapper);
    }
}
