package cn.neusoft.controller;

import cn.neusoft.annotation.Authority;
import cn.neusoft.constants.Constants;
import cn.neusoft.common.Result;
import cn.neusoft.entity.AuthorityType;
import cn.neusoft.entity.Address;
import cn.neusoft.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Authority(AuthorityType.requireLogin)
@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Resource
    private AddressService addressService;

    /*
    查询
    */
    @GetMapping("/{userId}")
    public Result findAllById(@PathVariable Long userId) {
        return Result.success(addressService.findAllById(userId));
    }

    @GetMapping
    public Result findAll() {
        List<Address> list = addressService.list();
        return Result.success(list);
    }


    /*
    保存
    */
    @PostMapping
    public Result save(@RequestBody Address address) {
        boolean b = addressService.saveOrUpdate(address);
        if(b){
            return Result.success();
        }else{
            return Result.error(Constants.CODE_500,"保存地址失败");
        }

    }

    @PutMapping
    public Result update(@RequestBody Address address) {
        addressService.updateById(address);
        return Result.success();
    }

    /*
    删除
    */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.success();
    }





}
