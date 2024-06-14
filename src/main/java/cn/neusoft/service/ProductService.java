package cn.neusoft.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.neusoft.constants.Constants;
import cn.neusoft.constants.RedisConstants;
import cn.neusoft.entity.Product;
import cn.neusoft.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.entity.GoodStandard;
import cn.neusoft.entity.dto.GoodDTO;
import cn.neusoft.mapper.ProductMapper;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private RedisTemplate<String, Product> redisTemplate;

    //查询一个商品的信息
    public Product getGoodById(Long id) {
        String redisKey = RedisConstants.GOOD_TOKEN_KEY + id;
        //从redis中查，若有则返回
        ValueOperations<String, Product> valueOperations = redisTemplate.opsForValue();
        Product redisProduct = valueOperations.get(redisKey);
        if(redisProduct !=null){
            redisTemplate.expire(redisKey, RedisConstants.GOOD_TOKEN_TTL, TimeUnit.MINUTES);
            return redisProduct;
        }
        //若redis中没有则去数据库查
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getIsDelete,false);
        queryWrapper.eq(Product::getId,id);
        Product dbProduct = getOne(queryWrapper);
        if(dbProduct !=null){
            //将商品信息存入redis
            valueOperations.set(redisKey, dbProduct);
            redisTemplate.expire(redisKey, RedisConstants.GOOD_TOKEN_TTL, TimeUnit.MINUTES);
            return dbProduct;
        }
        //数据库中没有则返回异常
        throw new ServiceException(Constants.NO_RESULT,"无结果");

    }
    //查询商品的规格
    public String getStandard(int id){
        List<GoodStandard> standards = productMapper.getStandardById(id);
        if(standards.size()==0){
            throw new ServiceException(Constants.NO_RESULT,"无结果");
        }
        return JSON.toJSONString(standards);
    }
    //查询某商品的最低规格价
    public BigDecimal getMinPrice(Long id){
        return productMapper.getMinPrice(id);
    }
    //查询全部（首页推荐商品）
    public List<GoodDTO> findFrontGoods() {
        return productMapper.findFrontGoods();
    }


    //假删除
    public void deleteGood(Long id) {
        redisTemplate.delete(RedisConstants.GOOD_TOKEN_KEY+id);
        productMapper.fakeDelete(id);
    }
    //保存商品信息
    public Long saveOrUpdateGood(Product product) {
        System.out.println(product);
        if(product.getId()==null){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            product.setCreateTime(df.format(LocalDateTime.now()));
            productMapper.insertGood(product);
        }else{
            saveOrUpdate(product);
            redisTemplate.delete(RedisConstants.GOOD_TOKEN_KEY + product.getId());
        }
        return product.getId();
    }

    public boolean setRecommend(Long id,Boolean isRecommend) {
        LambdaUpdateWrapper<Product> goodsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper.eq(Product::getId,id)
                .set(Product::getRecommend,isRecommend);
        return update(goodsLambdaUpdateWrapper);
    }

    public List<Product> getSaleRank(int num) {
        return productMapper.getSaleRank(num);
    }


    public void update(Product product) {
        updateById(product);
        redisTemplate.delete(RedisConstants.GOOD_TOKEN_KEY + product.getId());
    }
    //分页查询
    public IPage<GoodDTO> findPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Product> query = Wrappers.<Product>lambdaQuery().orderByDesc(Product::getId);
        //对名称和描述进行模糊查询
        if (StrUtil.isNotBlank(searchText)) {
            query.like(Product::getName, searchText).or().like(Product::getDescription,searchText).or().eq(Product::getId,searchText);
        }
        if(categoryId != null){
            query.eq(Product::getCategoryId,categoryId);
        }
        //筛除掉已被删除的商品
        query.eq(Product::getIsDelete,false);
        IPage<Product> page = this.page(new Page<>(pageNum, pageSize), query);
        //把good转为dto
        IPage<GoodDTO> goodDTOPage = page.convert(good -> {
            GoodDTO goodDTO = new GoodDTO();
            BeanUtil.copyProperties(good, goodDTO);
            return goodDTO;
        });
        for (GoodDTO good : goodDTOPage.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }
        return goodDTOPage;
    }
    public IPage<Product> findFullPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Product> query = Wrappers.<Product>lambdaQuery().orderByDesc(Product::getId);
        //对名称和描述进行模糊查询
        if (StrUtil.isNotBlank(searchText)) {
            query.like(Product::getName, searchText).or().like(Product::getDescription,searchText).or().eq(Product::getId,searchText);
        }
        if(categoryId != null){
            query.eq(Product::getCategoryId,categoryId);
        }
        //筛除掉已被删除的商品
        query.eq(Product::getIsDelete,false);
        IPage<Product> page = this.page(new Page<>(pageNum, pageSize), query);
        for (Product product : page.getRecords()) {
            //附上最低价格
            product.setPrice(getMinPrice(product.getId()));
        }
        return page;
    }
}
