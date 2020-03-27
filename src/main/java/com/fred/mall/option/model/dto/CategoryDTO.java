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
@ApiModel("品类")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(value = "0代码顶级分类", name = "parentId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "品类名", name = "name")
    private String name;

    @ApiModelProperty(value = "分类等级", name = "level")
    private Integer level;

    @ApiModelProperty(value = "优先级", name = "priority")
    private Integer priority;

    @ApiModelProperty(value = "", name = "detail")
    private String detail;

    @ApiModelProperty(value = "", name = "createdBy")
    private Long createdBy;

    @ApiModelProperty(value = "", name = "createdDate")
    private Long createdDate;

    @ApiModelProperty(value = "", name = "modifiedBy")
    private Long modifiedBy;

    @ApiModelProperty(value = "", name = "modifiedDate")
    private Long modifiedDate;
}
