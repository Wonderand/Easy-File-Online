package com.huzhirong.efo.entity;

import com.huzhirong.efo.util.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 分类表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private int id;

    /**
     * 分类名称
     */
    private String name;

    private Timestamp createTime;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return BeanUtils.toPrettyJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
