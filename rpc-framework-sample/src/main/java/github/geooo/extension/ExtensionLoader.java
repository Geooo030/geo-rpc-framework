package github.geooo.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author zhaoqi.wang
 * @date 2021/2/18
 */
@Slf4j
public final class ExtensionLoader<T> {

    public static final String EXTENSION_CONFIG_DIRECTORY = "META-INF/extensions/";
    public static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    public static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;
    private final Map<String, Holder<Object>> cacheInstance = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();


    public ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <V> ExtensionLoader<V> getExtensionLoader(Class<V> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Extension must not be null");
        }
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Extension type must be Interface");
        }
        if (clazz.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension must be annotated by @SPI");
        }
        ExtensionLoader<V> extensionLoader = (ExtensionLoader<V>) EXTENSION_LOADERS.get(clazz);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz));
            extensionLoader = (ExtensionLoader<V>) EXTENSION_LOADERS.get(clazz);
        }
        return extensionLoader;

    }

    public T getExtension(String name) {
        if (name == null || StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Extension's name must not be null");
        }
        Holder<Object> holder = cacheInstance.get(name);
        if (holder == null) {
            cacheInstance.putIfAbsent(name, new Holder<>());
            holder = cacheInstance.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createInstance(name);
                    holder.set(instance);
                }
            }
        }
        return (T)instance;
    }

    private T createInstance(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("No such extension of this name" + "<" + name + ">");
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            // scan all the type's config classes and new a new instance
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cacheClasses.get();
        // Load all the extension class which name equals "type"'s class path
        if (classes == null) {
            synchronized (cacheClasses) {
                classes = cacheClasses.get();
                if (classes == null) {
                    classes = new HashMap<>();
                    loadDirectory(classes);
                    cacheClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private void loadDirectory(Map<String, Class<?>> classes) {
        // file's name = SPI Interface's Class . getName()
        String fileName = ExtensionLoader.EXTENSION_CONFIG_DIRECTORY +type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while(urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadClass(url, classLoader, classes);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loadClass(URL url, ClassLoader classLoader, Map<String, Class<?>> classes) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8));
            String classMsg;
            // read every line of file
            while ((classMsg = reader.readLine()) != null) {
                // ignore the msg which after '#'
                int index = classMsg.indexOf("#");
                if (index >= 0) {
                    classMsg = classMsg.substring(0, index);
                }
                classMsg = classMsg.trim();
                if (!StringUtils.isEmpty(classMsg)) {
                    final int split = classMsg.indexOf("=");
                    String name = classMsg.substring(0, split).trim();
                    String clazzName = classMsg.substring(split + 1).trim();
                    // throw ClassNotFound Exception if can't find the className
                    Class<?> clazz = classLoader.loadClass(clazzName);
                    classes.put(name, clazz);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


}
