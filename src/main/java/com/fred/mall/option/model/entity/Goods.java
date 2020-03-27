package com.fred.mall.option.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Goods extends BaseEntity implements Serializable {

    /**
     * 
     */
    private Long productId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private String stock;

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
