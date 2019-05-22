package com.blog.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Created by yuguidong on 2018/9/21.
 */
public class BaseEntity extends Model<BaseEntity> {

    private static final long serialVersionUID = 1L;
    /**
     * 实体Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static Set<Long> getIdSetBySelfCollection(Collection<? extends BaseEntity> baseEntityCollection) {
        Set<Long> idSet = new HashSet<Long>();
        if (CollectionUtils.isNotEmpty(baseEntityCollection)) {
            for (BaseEntity baseEntity : baseEntityCollection) {
                idSet.add(baseEntity.getId());
            }
        }
        return idSet;
    }

    public static List<Long> getIdListBySelfCollection(Collection<? extends BaseEntity> baseEntityCollection) {
        List<Long> idSet = new LinkedList<>();//频繁执行插入操作,所以定义为LinkedList
        if (CollectionUtils.isNotEmpty(baseEntityCollection)) {
            for (BaseEntity baseEntity : baseEntityCollection) {
                idSet.add(baseEntity.getId());
            }
        }
        return idSet;
    }

    public static Map<Long, ? extends BaseEntity> getIdAndSelfMapBySelfCollection(Collection<? extends BaseEntity>
                                                                                          baseEntityCollection) {
        Map<Long, BaseEntity> idAndSelfMap = new HashMap<Long, BaseEntity>();
        if (CollectionUtils.isNotEmpty(baseEntityCollection)) {
            for (BaseEntity baseEntity : baseEntityCollection) {
                idAndSelfMap.put(baseEntity.getId(), baseEntity);
            }
        }
        return idAndSelfMap;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
