package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public class BaseServiceImpl<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements BaseService<T> {
    @Override
    public void join(Map<String, Object> map, String field) {

    }

    @Override
    public void join(List<Map<String, Object>> datas, String field) {
        if(CollectionUtil.isNotEmpty(datas)){
            datas.forEach(map -> {
                this.join(map,field);
            });
        }
    }

    @Override
    public void join(IPage pageData, String field) {
        List<Map<String, Object>> records = pageData.getRecords();
        this.join(records,field);
    }
}
