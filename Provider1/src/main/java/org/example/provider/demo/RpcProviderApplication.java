package org.example.provider.demo;

import org.example.rpc.annotation.EnableProviderRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProviderRpc
public class RpcProviderApplication {

    public static void main(String[] args) {

        SpringApplication.run(RpcProviderApplication.class, args);

    }
}
