package org.example.provider.demo.services;

import org.example.rpc.annotation.RpcService;
import org.example.rpc.demo.Test2Service;

@RpcService
public class Test2ServiceImpl implements Test2Service {

    @Override
    public String test(String key) {
        System.out.println("服务提供2 test2 测试成功 :" + key);
        return key;
    }
}
