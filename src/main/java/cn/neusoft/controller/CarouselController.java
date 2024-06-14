package cn.neusoft.controller;

import cn.neusoft.entity.Product;
import com.auth0.jwt.JWT;
import cn.neusoft.annotation.Authority;
import cn.neusoft.common.Result;
import cn.neusoft.entity.AuthorityType;
import cn.neusoft.service.ProductService;
import cn.neusoft.service.UserService;
import cn.neusoft.entity.Carousel;
import cn.neusoft.service.CarouselService;
import cn.neusoft.entity.User;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/carousel")
public class CarouselController {
    @Resource
    private CarouselService carouselService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private ProductService productService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    /*
    查询
    */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(carouselService.getById(id));
    }

    @GetMapping
    public Result findAll() {
        List<Carousel> list = carouselService.getAllCarousel();
        return Result.success(list);
    }

    /*
    保存
    */
    @Authority(AuthorityType.requireAuthority)
    @PostMapping
    public Result save(@RequestBody Carousel carousel) {
        Product product = productService.getById(carousel.getGoodId());
        if(product == null) {
            return Result.error("400", "商品id错误，未查询到商品id = " + carousel.getGoodId());
        }
        carouselService.saveOrUpdate(carousel);
        return Result.success();
    }
    @Authority(AuthorityType.requireAuthority)
    @PutMapping
    public Result update(@RequestBody Carousel carousel) {
        Product product = productService.getById(carousel.getGoodId());
        if(product == null) {
            return Result.error("400", "商品id错误，未查询到商品id = " + carousel.getGoodId());
        }
        carouselService.updateById(carousel);
        return Result.success();
    }

    /*
    删除
    */
    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        carouselService.removeById(id);
        return Result.success();
    }





}
