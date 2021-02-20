package github.geooo.registry.zk;

import github.geooo.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author zhaoqi.wang
 * @date 2021/2/20
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        log.info("获得Zookeeper服务注册实例（实现ServiceRegistry接口）");

    }

}
