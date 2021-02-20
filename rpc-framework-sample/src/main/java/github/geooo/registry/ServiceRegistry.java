package github.geooo.registry;

import github.geooo.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author zhaoqi.wang
 * @date 2021/2/20
 */
@SPI
public interface ServiceRegistry {

    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
