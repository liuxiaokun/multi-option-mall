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
public class ProductOptionValue extends BaseEntity implements Serializable {

    /**
     * 
     */
    private Long productId;

    /**
     * 
     */
    private Long optionId;

    /**
     * 
     */
    private String value;

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
