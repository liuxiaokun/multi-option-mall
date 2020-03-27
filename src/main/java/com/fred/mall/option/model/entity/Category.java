package com.fred.mall.option.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity implements Serializable {

    /**
     * 0代码顶级分类
     */
    private Long parentId;

    /**
     * 品类名
     */
    private String name;

    /**
     * 分类等级
     */
    private Integer level;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 
     */
    private String detail;

    /**
     * 
     */
    private Long createdBy;

    /**
     * 
     */
    private Long createdDate;

    /**
     * 
     */
    private Long modifiedBy;

    /**
     * 
     */
    private Long modifiedDate;
}
