package github.geooo.annotation;

import java.lang.annotation.*;

/**
 * RPC service annotation, marked on the service implementation class
 * which can be scanned and inject into the Spring Container
 *
 * @author george.wang
 * @date 2021/2/5
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    // Server's name
    String name() default "";

    // Server's version
    String version() default "";
}
