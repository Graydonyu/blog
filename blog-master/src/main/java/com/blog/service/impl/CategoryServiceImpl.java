package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.blog.entity.Category;
import com.blog.mapper.CategoryMapper;
import com.blog.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public void join(Map<String,Object> map,String field){
        if(CollectionUtil.isEmpty(map) || map.get(field) == null){
            return;
        }

        Map<String, Object> joinColumns = new HashMap<>();
        //字段的值
        String linkfieldValue = map.get(field).toString();
        Category category = this.getById(linkfieldValue);

        if(category != null){
            joinColumns.put("id", category.getId());
            joinColumns.put("name", category.getName());
            joinColumns.put("icon", category.getIcon());
        }

        map.put("category", joinColumns);
    }

}
