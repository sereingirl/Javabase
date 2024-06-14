package cn.neusoft.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.neusoft.entity.Icon;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.entity.IconCategory;
import cn.neusoft.mapper.IconCategoryMapper;
import cn.neusoft.mapper.IconMapper;
import cn.neusoft.utils.BaseApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class IconService extends ServiceImpl<IconMapper, Icon> {

    @Resource
    private IconMapper iconMapper;

    @Resource
    private IconCategoryMapper iconCategoryMapper;

    public List<Icon> getIconCategoryMapList() {
        return iconMapper.getIconCategoryMapList();
    }

    /**
     * 删除上级分类
     *
     * @param id id
     */
    public Map<String, Object> deleteById(Long id) {
        // 检查是否包含下级分类
        Long count = iconCategoryMapper.selectCount(
                new QueryWrapper<IconCategory>().eq("icon_id", id)
        );
        if (count > 0) {
            return BaseApi.error("该上级分类存在下级分类，请删除所有下级分类再尝试删除");
        }
        super.removeById(id);
        return BaseApi.success();
    }
}
