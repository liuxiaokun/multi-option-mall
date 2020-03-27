package com.fred.mall.option.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@ApiModel("sku商品")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "", name = "productId")
    private Long productId;

    @ApiModelProperty(value = "", name = "name")
    private String name;

    @ApiModelProperty(value = "", name = "code")
    private String code;

    @ApiModelProperty(value = "", name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "", name = "stock")
    private String stock;

    @ApiModelProperty(value = "", name = "createdBy")
    private Long createdBy;

    @ApiModelProperty(value = "", name = "createdDate")
    private Long createdDate;

    @ApiModelProperty(value = "", name = "modifiedBy")
    private Long modifiedBy;

    @ApiModelProperty(value = "", name = "modifiedDate")
    private Long modifiedDate;

    private List<Map<String, Object>> combines;
}
