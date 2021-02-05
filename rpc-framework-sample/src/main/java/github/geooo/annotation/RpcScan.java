package github.geooo.annotation;

import github.geooo.spring.RpcScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * scan the rpc special annotation
 * ch : 扫描在 @RpcScan 指定扫描包中被 @RpcService 标记的类
 * @author george.wang
 * @date 2021/2/4
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScannerRegistrar.class)
// @Import 是结合实现 Spring ImportBeanDefinitionRegistrar 接口 进行扫包，将用 @RpcScan标记的RPC服务类进行扫描且注入Spring容器
@Documented
public @interface RpcScan {

    String[] basePackage();

}
