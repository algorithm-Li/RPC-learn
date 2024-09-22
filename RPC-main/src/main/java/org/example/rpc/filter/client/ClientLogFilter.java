package org.example.rpc.filter.client;

import org.example.rpc.filter.ClientBeforeFilter;
import org.example.rpc.filter.FilterData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description: 日志
 */
public class ClientLogFilter implements ClientBeforeFilter {

    private Logger logger = LoggerFactory.getLogger(ClientLogFilter.class);

    @Override
    public void doFilter(FilterData filterData) {
        logger.info(filterData.toString());
    }
}
