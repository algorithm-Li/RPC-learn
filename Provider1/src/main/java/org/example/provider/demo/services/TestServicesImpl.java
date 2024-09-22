package org.example.provider.demo.services;


import org.example.rpc.annotation.RpcService;
import org.example.rpc.demo.TestService;

@RpcService
public class TestServicesImpl implements TestService {

    @Override
    public void test(String key) {
        System.out.println(1/0);
        // 爆异常，服务不可用测试
        System.out.println("服务提供1 test 测试成功  :" + key);
    }

    @Override
    public void test2(String key) {
        System.out.println("服务提供1 test2 测试成功  :" + key);
    }
}
