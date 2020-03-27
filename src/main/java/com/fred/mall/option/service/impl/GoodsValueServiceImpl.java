package com.fred.mall.option.service.impl;

import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.fred.mall.option.dao.GoodsValueDAO;
import com.fred.mall.option.exception.BizException;
import com.fred.mall.option.model.dto.GoodsValueDTO;
import com.fred.mall.option.model.entity.GoodsValue;
import com.fred.mall.option.service.GoodsValueService;
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
public class GoodsValueServiceImpl implements GoodsValueService {

    private final GoodsValueDAO goodsValueDAO;

    @Autowired
    public GoodsValueServiceImpl(GoodsValueDAO goodsValueDAO) {
        this.goodsValueDAO = goodsValueDAO;
    }

    @Override
    public void saveGoodsValue(@NonNull GoodsValue goodsValue) throws BizException {
        log.info("save GoodsValue:{}", goodsValue);
        if (goodsValueDAO.insert(goodsValue) != 1) {
            log.error("insert error, data:{}", goodsValue);
            throw new BizException("Insert GoodsValue Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsValueList(@NonNull List<GoodsValue> goodsValueList) throws BizException {

        if (goodsValueList.size() == 0) {
            throw new BizException("参数长度不能为0");
        }
        int rows = goodsValueDAO.insertList(goodsValueList);

        if (rows != goodsValueList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, goodsValueList.size());
            throw new BizException("批量保存异常");
        }
    }

    @Override
    public void updateGoodsValue(@NonNull GoodsValue goodsValue) {
        log.info("full update GoodsValue:{}", goodsValue);
        goodsValueDAO.update(goodsValue);
    }

    @Override
    public void updateGoodsValueSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        goodsValueDAO.updatex(params);
    }

    @Override
    public void logicDeleteGoodsValue(@NonNull Long id, @NonNull Long userId) throws BizException {
        log.info("逻辑删除，数据id:{}, 用户id:{}", id, userId);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", userId);
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = goodsValueDAO.delete(params);

        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException("删除异常.");
        }
    }

    @Override
    public void deleteGoodsValue(@NonNull Long id) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = goodsValueDAO.pdelete(params);

        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException("删除失败");
        }
    }

    @Override
    public GoodsValueDTO findGoodsValueById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        GoodsValue goodsValue = goodsValueDAO.selectOne(params);
        GoodsValueDTO goodsValueDTO = new GoodsValueDTO();

        if (null != goodsValue) {
            BeanUtils.copyProperties(goodsValue, goodsValueDTO);
        }
        return goodsValueDTO;
    }

    @Override
    public GoodsValueDTO findOneGoodsValue(Map<String, Object> params) {
        log.info("find one params:{}", params);
        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry -> (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        GoodsValue goodsValue = goodsValueDAO.selectOne(params);
        GoodsValueDTO goodsValueDTO = new GoodsValueDTO();
        if (null != goodsValue) {
            BeanUtils.copyProperties(goodsValue, goodsValueDTO);
        }
        return goodsValueDTO;
    }

    @Override
    public List<GoodsValueDTO> find(Map<String, Object> params,
        Vector<SortingContext> scs, PagingContext pc) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        params.put("pc", pc);
        params.put("scs", scs);
        List<GoodsValue> goodsValueList = goodsValueDAO.select(params);
        List<GoodsValueDTO> resultList = new ArrayList<>();
        goodsValueList.forEach(tem -> {
            GoodsValueDTO goodsValueDTO = new GoodsValueDTO();
            BeanUtils.copyProperties(tem, goodsValueDTO);
            resultList.add(goodsValueDTO);
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
        return goodsValueDAO.selectMap(params);
    }

    @Override
    public int count(Map<String, Object> params) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                    (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return goodsValueDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = goodsValueDAO.groupCount(conditions);
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
        return goodsValueDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = goodsValueDAO.groupSum(conditions);
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
