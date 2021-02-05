package github.geooo.config;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * config the project's variable -> server.properties
 * ch : 将项目需要配置的配置到 server.properties
 * @author george.wang
 * @date 2021/2/4
 */
public class Config {

    public static final String SPRING_BEAN_BASE_PACKAGE;
    // RpcScan扫包默认路径
    public static final String SPRING_SCAN_DEFAULT_PATH;
    public static final String BASE_PACKAGE_ATTRIBUTE_NAME;

    static {
        Properties properties = new Properties();
        URL resource = Config.class.getClassLoader().getResource("server.properties");
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(resource).toURI());
            properties.load(new FileInputStream(path.toFile()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        BASE_PACKAGE_ATTRIBUTE_NAME = properties.getProperty("bean.package.attribute.name");
        SPRING_BEAN_BASE_PACKAGE = properties.getProperty("spring.bean.base.package");
        SPRING_SCAN_DEFAULT_PATH = properties.getProperty("bean.scan.default.path");

    }
}
