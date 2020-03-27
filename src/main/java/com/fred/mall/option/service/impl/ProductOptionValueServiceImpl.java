package com.fred.mall.option.service.impl;

import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.fred.mall.option.dao.ProductOptionValueDAO;
import com.fred.mall.option.exception.BizException;
import com.fred.mall.option.model.dto.ProductOptionValueDTO;
import com.fred.mall.option.model.entity.ProductOptionValue;
import com.fred.mall.option.service.ProductOptionValueService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@Service
@Slf4j
public class ProductOptionValueServiceImpl implements ProductOptionValueService {

    private final ProductOptionValueDAO productOptionValueDAO;

    @Autowired
    public ProductOptionValueServiceImpl(ProductOptionValueDAO productOptionValueDAO) {
        this.productOptionValueDAO = productOptionValueDAO;
    }

    @Override
    public void saveProductOptionValue(@NonNull ProductOptionValue productOptionValue) throws BizException {
        log.info("save ProductOptionValue:{}", productOptionValue);
        if (productOptionValueDAO.insert(productOptionValue) != 1) {
            log.error("insert error, data:{}", productOptionValue);
            throw new BizException("Insert ProductOptionValue Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductOptionValueList(@NonNull List<ProductOptionValue> productOptionValueList) throws BizException {

        if (productOptionValueList.size() == 0) {
            throw new BizException("参数长度不能为0");
        }
        int rows = productOptionValueDAO.insertList(productOptionValueList);

        if (rows != productOptionValueList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, productOptionValueList.size());
            throw new BizException("批量保存异常");
        }
    }

    @Override
    public void updateProductOptionValue(@NonNull ProductOptionValue productOptionValue) {
        log.info("full update ProductOptionValue:{}", productOptionValue);
        productOptionValueDAO.update(productOptionValue);
    }

    @Override
    public void updateProductOptionValueSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        productOptionValueDAO.updatex(params);
    }

    @Override
    public void logicDeleteProductOptionValue(@NonNull Long id, @NonNull Long userId) throws BizException {
        log.info("逻辑删除，数据id:{}, 用户id:{}", id, userId);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", userId);
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = productOptionValueDAO.delete(params);

        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException("删除异常.");
        }
    }

    @Override
    public void deleteProductOptionValue(@NonNull Long id) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = productOptionValueDAO.pdelete(params);

        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException("删除失败");
        }
    }

    @Override
    public ProductOptionValueDTO findProductOptionValueById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        ProductOptionValue productOptionValue = productOptionValueDAO.selectOne(params);
        ProductOptionValueDTO productOptionValueDTO = new ProductOptionValueDTO();

        if (null != productOptionValue) {
            BeanUtils.copyProperties(productOptionValue, productOptionValueDTO);
        }
        return productOptionValueDTO;
    }

    @Override
    public ProductOptionValueDTO findOneProductOptionValue(Map<String, Object> params) {
        log.info("find one params:{}", params);
        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry -> (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        ProductOptionValue productOptionValue = productOptionValueDAO.selectOne(params);
        ProductOptionValueDTO productOptionValueDTO = new ProductOptionValueDTO();
        if (null != productOptionValue) {
            BeanUtils.copyProperties(productOptionValue, productOptionValueDTO);
        }
        return productOptionValueDTO;
    }

    @Override
    public List<ProductOptionValueDTO> find(Map<String, Object> params,
        Vector<SortingContext> scs, PagingContext pc) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        params.put("pc", pc);
        params.put("scs", scs);
        List<ProductOptionValue> productOptionValueList = productOptionValueDAO.select(params);
        List<ProductOptionValueDTO> resultList = new ArrayList<>();
        productOptionValueList.forEach(tem -> {
            ProductOptionValueDTO productOptionValueDTO = new ProductOptionValueDTO();
            BeanUtils.copyProperties(tem, productOptionValueDTO);
            resultList.add(productOptionValueDTO);
        });

        return resultList;
    }

    @Override
    public List<Map> findMap(Map<String, Object> params, Vector<SortingContext> scs,
                          PagingContext pc, String... columns) throws BizException {
        if (columns.length == 0) {
            throw new BizException("columns长度不能为0");
        }
        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                    (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        params.put("pc", pc);
        params.put("scs", scs);
        params.put("columns", columns);
        return productOptionValueDAO.selectMap(params);
    }

    @Override
    public int count(Map<String, Object> params) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                    (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return productOptionValueDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = productOptionValueDAO.groupCount(conditions);
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> m : maps) {
            String key = m.get("group") != null ? m.get("group").toString() : "group";
            Object value = m.get("count");
            int count = 0;
            if (StringUtils.hasLength(value.toString())) {
                count = Integer.parseInt(value.toString());
            }
            map.put(key, count);
        }
        return map;
    }

    @Override
    public Double sum(String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("sumfield", sumField);
        return productOptionValueDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = productOptionValueDAO.groupSum(conditions);
        Map<String, Double> map = new LinkedHashMap<String, Double>();
        for (Map<String, Object> m : maps) {
            String key = m.get("group") != null ? m.get("group").toString() : "group";
            Object value = m.get("sum");
            double sum = 0d;
            if (StringUtils.hasLength(value.toString())) {
                sum = Double.parseDouble(value.toString());
            }
            map.put(key, sum);
        }
        return map;
    }
}
