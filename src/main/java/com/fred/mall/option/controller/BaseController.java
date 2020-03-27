package com.fred.mall.option.controller;

import com.byx.framework.core.context.AppContext;
import com.byx.framework.core.domain.PagingContext;
import com.byx.framework.core.domain.SortingContext;
import com.byx.framework.core.utils.StringUtil;
import com.fred.mall.option.model.entity.BaseEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxiaokun
 * @version 0.0.1
 * @since 2020/03/26
 */
public class BaseController<T extends BaseEntity> {

    private static final Pattern SC_EXTRACTION = Pattern.compile("(?<field>[0-9a-zA-z]+)\\((?<sort>[0-9a-zA-z]+)\\)", 2);
    private static final int MAX_PAGE_SIZE = 1000;
    private static final String SEPARATOR = ",";

    protected T packAddBaseProps(T base, HttpServletRequest request) {
        long now = System.currentTimeMillis();
        Long currentLoginId = getUserId(request);
        base.setId(AppContext.IdGen.nextId());
        base.setCreatedBy(currentLoginId);
        base.setCreatedDate(now);
        base.setIp(getIp(request));
        base.setStatus(1);

        return base;
    }

    protected T packModifyBaseProps(T base, HttpServletRequest request) {
        base.setModifiedBy(getUserId(request));
        base.setModifiedDate(System.currentTimeMillis());
        base.setIp(getIp(request));
        return base;
    }

    public Long getUserId(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");

        Claims claims = Jwts.parser()
                .setSigningKey("my-secret".getBytes())
                .parseClaimsJws(jwt.replace("Bearer ", ""))
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 获取排序字段
     * 采用固定格式：scs=type(asc),enabled(desc)表示先按type升序排，再按enabled降序排
     */
    public Vector<SortingContext> getSortingContext(HttpServletRequest req) {
        Vector<SortingContext> scs = new Vector<>();
        String scsValue = req.getParameter("scs");
        if (StringUtils.hasLength(scsValue)) {
            String[] sortFields = scsValue.split(SEPARATOR);

            for (String scStr : sortFields) {
                Matcher matcher = SC_EXTRACTION.matcher(scStr);
                if (matcher.matches()) {
                    String field = matcher.group("field");
                    String o = matcher.group("sort");
                    String order = "asc".equalsIgnoreCase(o) ? "ASC" : "DESC";
                    if (StringUtils.hasLength(field) && StringUtils.hasLength(order)) {
                        SortingContext sc = new SortingContext(field, order);
                        scs.add(sc);
                    }
                }
            }
        }
        return scs;
    }

    /**
     * p = pageIndex 第几页
     * s = pageSize  一页几条
     */
    public PagingContext getPagingContext(HttpServletRequest req, Integer total) {

        PagingContext pc = new PagingContext();
        pc.setTotal(total);
        String s = req.getParameter("s");
        if (StringUtil.isNumeric(s)) {
            int size = Integer.parseInt(s);
            if (size > 0 && size < MAX_PAGE_SIZE) {
                pc.setPageSize(size);
            } else {
                pc.setPageSize(total <= MAX_PAGE_SIZE ? total : MAX_PAGE_SIZE);
            }
        } else {
            pc.setPageSize(20);
        }
        String p = req.getParameter("p");
        if (StringUtil.isNumeric(p)) {
            pc.setPageIndex(Integer.parseInt(p));
        } else {
            pc.setPageIndex(1);
        }
        return pc;
    }

    public Map<String, Object> getConditionsMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(15);

        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            String parameterValue = request.getParameter(paraName);

            if (StringUtils.hasLength(paraName) && StringUtils.hasLength(parameterValue)) {
                map.put(paraName, parameterValue);
            }
        }
        return map;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
