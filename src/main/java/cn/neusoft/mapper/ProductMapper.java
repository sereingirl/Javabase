package cn.neusoft.mapper;

import cn.neusoft.entity.Product;
import cn.neusoft.entity.GoodStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.neusoft.entity.dto.GoodDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    @Select("select * from good_standard where good_id = #{id}")
    List<GoodStandard> getStandardById(int id);

    List<GoodDTO> findFrontGoods();

    @Update("update product set is_delete = 1 where id = #{id}")
    void fakeDelete(Long id);

    void insertGood(@Param("product") Product product);

    @Select("SELECT discount * MIN(price) FROM good_standard gs, product WHERE product.id = gs.good_id AND product.id = #{id} ")
    BigDecimal getMinPrice(Long id);

    boolean saleGood(@Param("id")Long goodId,@Param("count") int count,@Param("money") BigDecimal totalPrice);


    @Select("SELECT * FROM product WHERE is_delete = 0 ORDER BY sale_money DESC LIMIT 0,#{num}")
    List<Product> getSaleRank(int num);
}
