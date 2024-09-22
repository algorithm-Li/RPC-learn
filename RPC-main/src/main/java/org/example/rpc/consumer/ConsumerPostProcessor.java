package org.example.rpc.consumer;

import org.example.rpc.annotation.RpcReference;
import org.example.rpc.config.RpcProperties;
import org.example.rpc.filter.FilterConfig;
import org.example.rpc.filter.client.ClientLogFilter;
import org.example.rpc.protocol.serialization.SerializationFactory;
import org.example.rpc.registry.RegistryFactory;
import org.example.rpc.router.LoadBalancerFactory;
import org.example.rpc.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @description: 消费方后置处理器
 */
public class ConsumerPostProcessor implements BeanPostProcessor, EnvironmentAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(ClientLogFilter.class);

    RpcProperties rpcProperties;

    /**
     * 从配置文件中读取配置
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        RpcProperties properties = RpcProperties.getInstance();
        PropertiesUtils.init(properties,environment);
        rpcProperties = properties;
        logger.info("读取配置文件成功");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SerializationFactory.init();
        RegistryFactory.init();
        LoadBalancerFactory.init();
        FilterConfig.initClientFilter();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有字段
        final Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有字段找到 RpcReference 注解的字段
        // 一般是调用的服务上面有 RpcReference 注解
        for (Field field : fields) {
            if(field.isAnnotationPresent(RpcReference.class)){
                final RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                //获取这个接口
                final Class<?> aClass = field.getType();
                field.setAccessible(true);
                Object object = null;
                try {
                    // 创建代理对象，参数：param1：类加载器，param2：interface就是自己，param3：InvocationHandler
                    // RpcInvokerProxy 实现了 InvocationHandler
                    object = Proxy.newProxyInstance(
                            aClass.getClassLoader(),
                            new Class<?>[]{aClass},
                            new RpcInvokerProxy(rpcReference.serviceVersion(),rpcReference.timeout(),rpcReference.faultTolerant(),
                                    rpcReference.loadBalancer(),rpcReference.retryCount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    // 将代理对象设置给字段
                    field.set(bean, object);
                    field.setAccessible(false);
                    logger.info(beanName + " field:" + field.getName() + "注入成功");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.info(beanName + " field:" + field.getName() + "注入失败");
                }
            }
        }
        return bean;
    }
}
