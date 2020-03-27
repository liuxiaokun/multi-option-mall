package com.fred.mall.option.controller;

import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.byx.framework.core.web.RO;
import com.fred.mall.option.exception.BizException;
import com.fred.mall.option.model.dto.GoodsValueDTO;
import com.fred.mall.option.model.entity.GoodsValue;
import com.fred.mall.option.service.GoodsValueService;
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

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@RestController
@RequestMapping(value = "/goods/value", name = "GoodsValue")
@Slf4j
@Api("GoodsValue")
public class GoodsValueController extends BaseController<GoodsValue> {

    private final GoodsValueService goodsValueService;

    @Autowired
    public GoodsValueController(GoodsValueService goodsValueService) {
        this.goodsValueService = goodsValueService;
    }

    @ApiOperation(value = "根据条件查询GoodsValue列表", notes = "包含查询条件，分页以及排序功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "每页的条数", paramType = "query"),
            @ApiImplicitParam(name = "p", value = "请求的页码", paramType = "query"),
            @ApiImplicitParam(name = "sc", value = "排序字段，格式：scs=name(asc),sc=age(desc),有序", paramType = "query"),
            })
    @GetMapping(name = "mall-multi-option-GoodsValue管理")
    public Object list(HttpServletRequest request, GoodsValueDTO goodsValueDTO) {
        log.info("list:{}", goodsValueDTO);

        Map<String, Object> params = getConditionsMap(request);
        List<GoodsValueDTO> list = new ArrayList<>();
        int total = goodsValueService.count(params);
        PagingContext pc = getPagingContext(request, total);
        Vector<SortingContext> scs = getSortingContext(request);
        if (total > 0) {
            list = goodsValueService.find(params, scs, pc);
        }
        return RO.success(list, pc, scs);
    }

    @ApiOperation(value = "查询GoodsValue", notes = "根据ID查询GoodsValue")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @GetMapping(value = "/{id}", name = "查看")
    public Object view(@PathVariable("id") Long id) {
        log.info("get goodsValue Id:{}", id);
        GoodsValueDTO goodsValueDTO = goodsValueService.findGoodsValueById(id);
        return RO.success(goodsValueDTO);
    }

    @ApiOperation(value = "新增GoodsValue", notes = "新增一条GoodsValue记录")
    @PostMapping(name = "创建")
    public Object create(@RequestBody GoodsValueDTO goodsValueDTO, HttpServletRequest request) {
        log.info("add goodsValue DTO:{}", goodsValueDTO);
        GoodsValue sourceGoodsValue = new GoodsValue();
        try {
            GoodsValue goodsValue = BeanUtil.copyProperties(goodsValueDTO, sourceGoodsValue);
            goodsValueService.saveGoodsValue(this.packAddBaseProps(goodsValue, request));
        } catch (BizException e) {
            log.error("add goodsValue failed,  goodsValueDTO: {}, error message:{}", goodsValueDTO, e.getMessage());
            return RO.error(e.getMessage());
        }
        return RO.success();
    }

    @ApiOperation(value = "修改GoodsValue", notes = "根据ID, 修改一条GoodsValue记录")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @PutMapping(value = "/{id}", name = "修改")
    public Object update(@PathVariable("id") Long id, @RequestBody GoodsValueDTO goodsValueDTO, HttpServletRequest request) {
        log.info("put modify id:{}, goodsValue DTO:{}", id, goodsValueDTO);
        GoodsValue goodsValue = new GoodsValue();
        goodsValue.setId(id);
        goodsValueService.updateGoodsValue(this.packModifyBaseProps(BeanUtil.copyProperties(goodsValueDTO, goodsValue), request));
        return RO.success();
    }

    @ApiOperation(value = "修改GoodsValue", notes = "根据ID, 部分修改一条GoodsValue记录")
    @ApiImplicitParam(name = "id", value = "主键ID", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @PatchMapping(value = "/{id}", name = "修改指定项")
    public Object updatex(@PathVariable("id") Long id, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        log.info("Patch modify GoodsValue Id:{}", id);
        params.put("modified_by", getUserId(request));
        params.put("modified_date", System.currentTimeMillis());

        Map<String, Object> conditions = new HashMap<>(1);
        conditions.put("id", id);
        goodsValueService.updateGoodsValueSelective(params, conditions);
        return RO.success();
    }

    @ApiOperation(value = "删除GoodsValue", notes = "根据ID, 逻辑删除一条GoodsValue记录")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", paramType = "path", required = true, example = "947153698855845888")
    @DeleteMapping(value = "/{id}", name = "删除")
    public Object remove(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("delete goodsValue, id:{}", id);

        try {
            goodsValueService.logicDeleteGoodsValue(id, this.getUserId(request));
        } catch (BizException e) {
            log.error("delete failed, goodsValue id: {}, error message:{}", id, e.getMessage());
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
        Double sum = goodsValueService.sum(sumField, params);
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
        Map<String, Double> result = goodsValueService.groupSum(groupField, sumField, params);
        return RO.success(result);
    }

    @ApiOperation(value = "按照指定字段分组计算指定字段的数目", notes = "根据指定的字段，用count函数计算符合条件记录字段的数目。")
    @ApiImplicitParam(name = "groupField", value = "分组的字段", dataType = "String", paramType = "path", required = true)
    @GetMapping(value = "/group/{groupField}/count", name = "统计分组字段数")
    public Object groupCount(@PathVariable String groupField, HttpServletRequest request) {
        log.info("groupField:{}", groupField);
        Map<String, Object> params = getConditionsMap(request);
        Map<String, Integer> result = goodsValueService.groupCount(groupField, params);
        return RO.success(result);
    }
}
