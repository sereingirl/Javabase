package cn.neusoft.service;

import cn.neusoft.entity.Cart;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.mapper.CartMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {

    @Resource
    private CartMapper cartMapper;

    public List<Map<String,Object>> selectByUserId(Long userId) {
         return cartMapper.selectByUserId(userId);
    }
}
