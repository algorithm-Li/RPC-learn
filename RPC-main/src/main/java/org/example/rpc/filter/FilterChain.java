package org.example.rpc.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 拦截器链
 */
public class FilterChain {

    //保存过滤器 数组 ArrayList
    private List<Filter> filters = new ArrayList<>();

    /**
     * 添加单个过滤器
     * @param filter
     */
    public void addFilter(Filter filter){
        filters.add(filter);
    }

    /**
     * 添加多个过滤器
     * @param filters
     */
    public void addFilter(List<Object> filters){
        for (Object filter : filters) {
            addFilter((Filter) filter);
        }
    }

    /**
     * Executes the filtering process by iterating through all filters in the chain and applying each one to the provided filter data.
     *
     * @param data the data that needs to be processed by the filters in the chain
     */
    public void doFilter(FilterData data){
        for (Filter filter : filters) {
            filter.doFilter(data);
        }
    }
}
