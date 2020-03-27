package com.fred.mall.option.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@ApiModel("GoodsValue")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsValueDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "", name = "goodsId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long goodsId;

    @ApiModelProperty(value = "", name = "productOptionValueId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productOptionValueId;

    @ApiModelProperty(value = "", name = "createdBy")
    private Long createdBy;

    @ApiModelProperty(value = "", name = "createdDate")
    private Long createdDate;

    @ApiModelProperty(value = "", name = "modifiedBy")
    private Long modifiedBy;

    @ApiModelProperty(value = "", name = "modifiedDate")
    private Long modifiedDate;
}
