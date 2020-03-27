package com.fred.mall.option.controller;

import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.byx.framework.core.web.RO;
import com.fred.mall.option.exception.BizException;
import com.fred.mall.option.model.dto.*;
import com.fred.mall.option.model.entity.Option;
import com.fred.mall.option.model.entity.Product;
import com.fred.mall.option.model.entity.ProductOptionValue;
import com.fred.mall.option.service.*;
import com.fred.mall.option.utils.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@RestController
@RequestMapping(value = "/product", name = "spu产品")
@Slf4j
@Api("spu产品")
public class ProductController extends BaseController<Product> {

    private final ProductService productService;
    private final GoodsService goodsService;
    private final GoodsValueService goodsValueService;
    private final OptionService optionService;
    private final ProductOptionValueService productOptionValueService;

    @Autowired
    public ProductController(ProductService productService, OptionService optionService,
                             ProductOptionValueService productOptionValueService, GoodsService goodsService,
                             GoodsValueService goodsValueService) {
        this.productService = productService;
        this.optionService = optionService;
        this.productOptionValueService = productOptionValueService;
        this.goodsService = goodsService;
        this.goodsValueService = goodsValueService;
    }

    @ApiOperation(value = "根据条件查询Product列表", notes = "包含查询条件，分页以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "sc", value = "排序字段，格式：scs=name(asc),sc=age(desc),有序", paramType = "query"),
    })
    @GetMapping(name = "mall-multi-option-spu产品管理")
    public Object list(HttpServletRequest request, ProductDTO productDTO) {
        log.info("list:{}", productDTO);

        Map<String, Object> params = getConditionsMap(request);
        List<ProductDTO> list = new ArrayList<>();
        int total = productService.count(params);
        PagingContext pc = getPagingContext(request, total);
        Vector<SortingContext> scs = getSortingContext(request);
        if (total > 0) {
            list = productService.find(params, scs, pc);
        }
        return RO.success(list, pc, scs);
    }

    @ApiOperation(value = "查询Product", notes = "根据ID查询Product")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @GetMapping(value = "/{id}", name = "查看")
    public Object view(@PathVariable("id") Long id) {
        log.info("get product Id:{}", id);
        ProductDTO productDTO = productService.findProductById(id);
        return RO.success(productDTO);
    }

    @ApiOperation(value = "新增Product", notes = "新增一条Product记录")
    @PostMapping(name = "创建")
    public Object create(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        log.info("add product DTO:{}", productDTO);
        Product sourceProduct = new Product();
        try {
            Product product = BeanUtil.copyProperties(productDTO, sourceProduct);
            productService.saveProduct(this.packAddBaseProps(product, request));
        } catch (BizException e) {
            log.error("add product failed,  productDTO: {}, error message:{}", productDTO, e.getMessage());
            return RO.error(e.getMessage());
        }
        return RO.success();
    }

    @ApiOperation(value = "修改Product", notes = "根据ID, 修改一条Product记录")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @PutMapping(value = "/{id}", name = "修改")
    public Object update(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO, HttpServletRequest request) {
        log.info("put modify id:{}, product DTO:{}", id, productDTO);
        Product product = new Product();
        product.setId(id);
        productService.updateProduct(this.packModifyBaseProps(BeanUtil.copyProperties(productDTO, product), request));
        return RO.success();
    }

    @ApiOperation(value = "修改Product", notes = "根据ID, 部分修改一条Product记录")
    @ApiImplicitParam(name = "id", value = "主键ID", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @PatchMapping(value = "/{id}", name = "修改指定项")
    public Object updatex(@PathVariable("id") Long id, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        log.info("Patch modify Product Id:{}", id);
        params.put("modified_by", getUserId(request));
        params.put("modified_date", System.currentTimeMillis());

        Map<String, Object> conditions = new HashMap<>(1);
        conditions.put("id", id);
        productService.updateProductSelective(params, conditions);
        return RO.success();
    }

    @ApiOperation(value = "删除Product", notes = "根据ID, 逻辑删除一条Product记录")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @DeleteMapping(value = "/{id}", name = "删除")
    public Object remove(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("delete product, id:{}", id);

        try {
            productService.logicDeleteProduct(id, this.getUserId(request));
        } catch (BizException e) {
            log.error("delete failed, product id: {}, error message:{}", id, e.getMessage());
            return RO.error(e.getMessage());
        }
        return RO.success();
    }

    @ApiOperation(value = "计算指定字段的和", notes = "用sum函数计算符合条件记录的指定字段的和。")
    @ApiImplicitParam(name = "sumField", value = "求和的字段", dataType = "String", paramType = "path", required = true)
    @GetMapping(value = "/sum/{sumField}", name = "计算指定字段的和")
    public Object sum(@PathVariable String sumField, HttpServletRequest request) {
        log.info("sumField:{}", sumField);
        Map<String, Object> params = getConditionsMap(request);
        Double sum = productService.sum(sumField, params);
        return RO.success(sum);
    }

    @ApiOperation(value = "按照指定字段分组并计算指定字段的和", notes = "根据指定的字段分组，然后用sum函数计算符合条件记录指定字段的和。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sumField", value = "计算字段", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "groupField", value = "分组字段", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/group/{groupField}/sum/{sumField}", name = "计算分组字段和")
    public Object groupSum(@PathVariable String groupField, @PathVariable String sumField, HttpServletRequest request) {
        log.info("groupField:{}, sumField:{}", groupField, sumField);
        Map<String, Object> params = getConditionsMap(request);
        Map<String, Double> result = productService.groupSum(groupField, sumField, params);
        return RO.success(result);
    }

    @ApiOperation(value = "按照指定字段分组计算指定字段的数目", notes = "根据指定的字段，用count函数计算符合条件记录字段的数目。")
    @ApiImplicitParam(name = "groupField", value = "分组的字段", dataType = "String", paramType = "path", required = true)
    @GetMapping(value = "/group/{groupField}/count", name = "统计分组字段数")
    public Object groupCount(@PathVariable String groupField, HttpServletRequest request) {
        log.info("groupField:{}", groupField);
        Map<String, Object> params = getConditionsMap(request);
        Map<String, Integer> result = productService.groupCount(groupField, params);
        return RO.success(result);
    }

    @GetMapping("/detail/{id}")
    public Object detail(@PathVariable Long id) {

        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setProduct(productService.findProductById(id));

        Map<String, Object> params = new HashMap<>();
        params.put("productId", id);
        List<ProductOptionValueDTO> productOptionValueDTOs = productOptionValueService.find(params, null, null);
        Set<Long> optionIdSet = productOptionValueDTOs.stream().map(ProductOptionValueDTO::getOptionId).collect(Collectors.toSet());


        List<OptionDTO> options = new ArrayList<>();

        for (Long aLong : optionIdSet) {
            OptionDTO optionDTO = optionService.findOptionById(aLong);
            Map<String, Object> valuesParams = new HashMap<>();
            valuesParams.put("optionId", aLong);
            valuesParams.put("productId", id);
            List<ProductOptionValueDTO> inners = productOptionValueService.find(valuesParams, null, null);
            optionDTO.setValues(inners);
            options.add(optionDTO);
        }
        productDetailDTO.setOptions(options);


        List<GoodsDTO> goodsDTOS = goodsService.find(params, null, null);

        for (GoodsDTO goodsDTO : goodsDTOS) {

            Map<String, Object> goodsDTOParam = new HashMap<>();
            goodsDTOParam.put("goodsId", goodsDTO.getId());
            List<GoodsValueDTO> goodsValueDTOS = goodsValueService.find(goodsDTOParam, null, null);

            List<Map<String, Object>> combines = new ArrayList<>();

            for (GoodsValueDTO goodsValueDTO : goodsValueDTOS) {
                Map<String, Object> comb = new HashMap<>();
                comb.put("option_id", productOptionValueService.findProductOptionValueById(goodsValueDTO.getProductOptionValueId()).getOptionId() + "");
                comb.put("value_id", goodsValueDTO.getProductOptionValueId() + "");
                combines.add(comb);
            }
            goodsDTO.setCombines(combines);
        }
        productDetailDTO.setGoods(goodsDTOS);

        return productDetailDTO;
    }
}
