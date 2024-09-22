package org.example.rpc.tolerant;

import org.example.rpc.spi.ExtensionLoader;

/**
 * @description: 集群容错工厂
 */
public class FaultTolerantFactory {

    public static FaultTolerantStrategy get(String faultTolerantStrategy) throws Exception {
        return ExtensionLoader.getInstance().get(faultTolerantStrategy);
    }

    public static void init() throws Exception {
        ExtensionLoader.getInstance().loadExtension(FaultTolerantStrategy.class);
    }

}
