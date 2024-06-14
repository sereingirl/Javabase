package cn.neusoft.controller;

import cn.neusoft.annotation.Authority;
import cn.neusoft.constants.Constants;
import cn.neusoft.common.Result;
import cn.neusoft.entity.AuthorityType;
import cn.neusoft.entity.Product;
import cn.neusoft.entity.Standard;
import cn.neusoft.service.ProductService;
import cn.neusoft.service.StandardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/good")
public class ProductController {
    @Resource
    private ProductService productService;

    @Resource
    private StandardService standardService;

    @Authority(AuthorityType.requireAuthority)
    @PostMapping
    public Result save(@RequestBody Product product) {
        System.out.println(product);
        return Result.success(productService.saveOrUpdateGood(product));
    }

    @Authority(AuthorityType.requireAuthority)
    @PutMapping
    public Result update(@RequestBody Product product) {
        productService.update(product);
        return Result.success();
    }

    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        productService.deleteGood(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(productService.getGoodById(id));
    }

    //获取商品的规格信息
    @GetMapping("/standard/{id}")
    public Result getStandard(@PathVariable int id) {
        return Result.success(productService.getStandard(id));
    }
    //查询推荐商品，即recommend=1
    @GetMapping
    public Result findAll() {

        return Result.success(productService.findFrontGoods());
    }
    //查询销量排行
    @GetMapping("/rank")
    public Result getSaleRank(@RequestParam int num){
        return Result.success(productService.getSaleRank(num));
    }
    //保存商品的规格信息
    @PostMapping("/standard")
    public Result saveStandard(@RequestBody List<Standard> standards, @RequestParam int goodId) {
        //先删除全部旧记录
        standardService.deleteAll(goodId);
        //然后插入新记录
        for (Standard standard : standards) {
            standard.setGoodId(goodId);
            if(!standardService.save(standard)){
                return Result.error(Constants.CODE_500,"保存失败");
            }
        }
        return Result.success();
    }

    //删除商品的规格信息
    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/standard")
    public Result delStandard(@RequestBody Standard standard) {
        boolean delete = standardService.delete(standard);
        if(delete) {
            return Result.success();
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    //修改商品的推荐字段
    @Authority(AuthorityType.requireAuthority)
    @GetMapping("/recommend")
    public Result setRecommend(@RequestParam Long id,@RequestParam Boolean isRecommend){
        return Result.success(productService.setRecommend(id,isRecommend));
    }

    @GetMapping("/page")
    public Result findPage(
                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "") String searchText,
                            @RequestParam(required = false) Integer categoryId) {

        return Result.success(productService.findPage(pageNum,pageSize,searchText,categoryId));
    }
    @GetMapping("/fullPage")
    public Result findFullPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false) Integer categoryId) {

        return Result.success(productService.findFullPage(pageNum,pageSize,searchText,categoryId));
    }

}
