package github.geooo.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * call these methods before creating the bean if the class is annotated
 * @author zhaoqi.wang
 * @date 2021/2/7
 */

@Component
@Slf4j
public class SpringBeanPostProcessor implements BeanPostProcessor {
    

}
