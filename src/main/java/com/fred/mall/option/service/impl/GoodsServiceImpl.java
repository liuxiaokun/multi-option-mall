package com.fred.mall.option.service.impl;

import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.fred.mall.option.dao.GoodsDAO;
import com.fred.mall.option.exception.BizException;
import com.fred.mall.option.model.dto.GoodsDTO;
import com.fred.mall.option.model.entity.Goods;
import com.fred.mall.option.service.GoodsService;
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
public class GoodsServiceImpl implements GoodsService {

    private final GoodsDAO goodsDAO;

    @Autowired
    public GoodsServiceImpl(GoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    @Override
    public void saveGoods(@NonNull Goods goods) throws BizException {
        log.info("save Goods:{}", goods);
        if (goodsDAO.insert(goods) != 1) {
            log.error("insert error, data:{}", goods);
            throw new BizException("Insert Goods Error!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsList(@NonNull List<Goods> goodsList) throws BizException {

        if (goodsList.size() == 0) {
            throw new BizException("参数长度不能为0");
        }
        int rows = goodsDAO.insertList(goodsList);

        if (rows != goodsList.size()) {
            log.error("数据库实际插入成功数({})与给定的({})不一致", rows, goodsList.size());
            throw new BizException("批量保存异常");
        }
    }

    @Override
    public void updateGoods(@NonNull Goods goods) {
        log.info("full update Goods:{}", goods);
        goodsDAO.update(goods);
    }

    @Override
    public void updateGoodsSelective(@NonNull Map<String, Object> dataMap, @NonNull Map<String, Object> conditionMap) {
        log.info("part update dataMap:{}, conditionMap:{}", dataMap, conditionMap);
        Map<String, Object> params = new HashMap<>(2);
        params.put("datas", dataMap);
        params.put("conditions", conditionMap);
        goodsDAO.updatex(params);
    }

    @Override
    public void logicDeleteGoods(@NonNull Long id, @NonNull Long userId) throws BizException {
        log.info("逻辑删除，数据id:{}, 用户id:{}", id, userId);
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("modifiedBy", userId);
        params.put("modifiedDate", System.currentTimeMillis());
        int rows = goodsDAO.delete(params);

        if (rows != 1) {
            log.error("逻辑删除异常, rows:{}", rows);
            throw new BizException("删除异常.");
        }
    }

    @Override
    public void deleteGoods(@NonNull Long id) throws BizException {
        log.info("物理删除, id:{}", id);
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        int rows = goodsDAO.pdelete(params);

        if (rows != 1) {
            log.error("删除异常, 实际删除了{}条数据", rows);
            throw new BizException("删除失败");
        }
    }

    @Override
    public GoodsDTO findGoodsById(@NonNull Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        Goods goods = goodsDAO.selectOne(params);
        GoodsDTO goodsDTO = new GoodsDTO();

        if (null != goods) {
            BeanUtils.copyProperties(goods, goodsDTO);
        }
        return goodsDTO;
    }

    @Override
    public GoodsDTO findOneGoods(Map<String, Object> params) {
        log.info("find one params:{}", params);
        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry -> (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        Goods goods = goodsDAO.selectOne(params);
        GoodsDTO goodsDTO = new GoodsDTO();
        if (null != goods) {
            BeanUtils.copyProperties(goods, goodsDTO);
        }
        return goodsDTO;
    }

    @Override
    public List<GoodsDTO> find(Map<String, Object> params,
        Vector<SortingContext> scs, PagingContext pc) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        params.put("pc", pc);
        params.put("scs", scs);
        List<Goods> goodsList = goodsDAO.select(params);
        List<GoodsDTO> resultList = new ArrayList<>();
        goodsList.forEach(tem -> {
            GoodsDTO goodsDTO = new GoodsDTO();
            BeanUtils.copyProperties(tem, goodsDTO);
            resultList.add(goodsDTO);
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
        return goodsDAO.selectMap(params);
    }

    @Override
    public int count(Map<String, Object> params) {

        if (params.size() > 0) {
            params = params.entrySet().stream().filter(entry ->
                    (StringUtils.hasLength(entry.getKey()) && null != entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return goodsDAO.count(params);
    }

    @Override
    public Map<String, Integer> groupCount(String group, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(1);
        }
        conditions.put("group", group);
        List<Map<String, Object>> maps = goodsDAO.groupCount(conditions);
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
        return goodsDAO.sum(conditions);
    }

    @Override
    public Map<String, Double> groupSum(String group, String sumField, Map<String, Object> conditions) {
        if (conditions == null) {
            conditions = new HashMap<>(2);
        }
        conditions.put("group", group);
        conditions.put("sumfield", sumField);
        List<Map<String, Object>> maps = goodsDAO.groupSum(conditions);
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
