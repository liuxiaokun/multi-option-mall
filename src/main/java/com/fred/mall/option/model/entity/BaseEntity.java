package com.fred.mall.option.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
@Data
public class BaseEntity implements Serializable{

    /**
     * 数据库表的主键, 生成策略基于雪花算法
     * 获取示例：<code>AppContext.IdGen.nextId()</>
     */
    private Long id;

    /**
     * 创建人ID，对应 t_user表的id.
     * 获取示例：<code>BaseController#getUserId(request)</code>
     */
    private Long createdBy;

    /**
     * 创建时间，存储时间戳。
     * 获取示例：<code>System.currentTimeMillis()</code>
     */
    private Long createdDate;

    /**
     * 最后修改人ID，对应t_user表的id。
     * 获取示例：<code>BaseController#getUserId(request)</code>
     */
    private Long modifiedBy;

    /**
     * 最后修改时间，存储时间戳。
     * 获取示例：<code>System.currentTimeMillis()</code>
     */
    private Long modifiedDate;

    /**
     * 此记录的状态，例如逻辑删除
     * 示例：1：正常：0：删除 等等。
     */
    private Integer status;

    /**
     * 数据最后编辑人的ip地址。
     */
    private String ip;
}
