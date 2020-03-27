package com.fred.mall.option.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fred.mall.option.model.entity.ProductOptionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@ApiModel("规格")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "品类ID", name = "categoryId")
    @JsonSerialize(using = ToStringSerializer.class)
    private String categoryId;

    @ApiModelProperty(value = "规格名称", name = "name")
    private String name;

    @ApiModelProperty(value = "", name = "createdBy")
    private Long createdBy;

    @ApiModelProperty(value = "", name = "createdDate")
    private Long createdDate;

    @ApiModelProperty(value = "", name = "modifiedBy")
    private Long modifiedBy;

    @ApiModelProperty(value = "", name = "modifiedDate")
    private Long modifiedDate;

    private List<ProductOptionValueDTO> values;
}
