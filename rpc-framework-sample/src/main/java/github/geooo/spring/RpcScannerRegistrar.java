package github.geooo.spring;

import github.geooo.annotation.RpcScan;
import github.geooo.annotation.RpcService;
import github.geooo.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * filter and scan @RpcService annotation's class which in the @RpcScan's marked package and inject them into the Spring Container
 * ch: 扫描在 @RpcScan 注解中的base包路径下的 含有 @RpcService 注解的类，并且将其注入进Spring容器中
 * @author george.wang
 * @date 2021/2/4
 */
@Slf4j
public class RpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] rpcScanPackages = new String[0];
        if (Objects.nonNull(rpcScanAnnotationAttributes)) {
            // 获取 @RpcScan 中的 base 标记的包路径
            rpcScanPackages = rpcScanAnnotationAttributes.getStringArray(Config.BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        // 若 RpcScan 参数中未配置则拿配置的默认路径
        if (rpcScanPackages.length == 0) {
            rpcScanPackages = new String[]{(Config.SPRING_SCAN_DEFAULT_PATH)};
        }
        ServerScanner springScanner = new ServerScanner(registry, Component.class);
        ServerScanner rpcScanner = new ServerScanner(registry, RpcService.class);
        // 扫描Spring工具类
        Set<BeanDefinitionHolder> springScanRes = springScanner.doScan(Config.SPRING_BEAN_BASE_PACKAGE);
        log.info("Spring工具类扫描结果为:{}", springScanRes);

        // 扫描 RpcService 类
        Set<BeanDefinitionHolder> rpcScanRes = rpcScanner.doScan(rpcScanPackages);
        log.info("RPC服务类扫描结果为：{}", rpcScanRes);

    }
}
