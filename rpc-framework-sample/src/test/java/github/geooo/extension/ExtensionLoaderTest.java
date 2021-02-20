package github.geooo.extension;


import github.geooo.registry.ServiceRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author zhaoqi.wang
 * @date 2021/2/20
 */
class ExtensionLoaderTest {

    @Test
    public void testExtension() {
        ServiceRegistry zkServiceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
        assertEquals(zkServiceRegistry.registerService("1", null), 200);

    }

}
