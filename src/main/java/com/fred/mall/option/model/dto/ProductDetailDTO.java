package com.fred.mall.option.model.dto;

import com.fred.mall.option.model.entity.Goods;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDTO {

    private ProductDTO product;

    private List<OptionDTO> options;

    private List<GoodsDTO> goods;

}
