package com.quillagua.springcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author laiyy
 * @date 19-4-7
 * @infomation
 */
@Configuration
@EnableConfigurationProperties(ConfigSupportProperties.class)
public class ConfigSupportConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 一定要注意加载顺序！！！
     *
     * bootstrap.yml 加载类：org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration 的加载顺序是 HIGHEST_PRECEDENCE+10，如果当前配置类再其之前加载，无法找到 bootstrap 配置文件中的信息
     * 继而无法加载到本地
     */
    private final Integer orderNumber = Ordered.HIGHEST_PRECEDENCE + 10;

    @Autowired(required = false)
    private List<PropertySourceLocator> propertySourceLocators = Collections.EMPTY_LIST;

    @Autowired
    private ConfigSupportProperties configSupportProperties;

    /**
     * 初始化操作
     */
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        // 判断是否开启 config server 管理配置
        if (!isHasCloudConfigLocator(this.propertySourceLocators)) {
            logger.info("Config server 管理配置未启用");
            return;
        }
        logger.info(">>>>>>>>>>>>>>> 检查 config Server 配置资源 <<<<<<<<<<<<<<<");
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        logger.info(">>>>>>>>>>>>> 加载 PropertySources 源：" + propertySources.size() + " 个");

        // 判断配置备份功能是否启用
        if (!configSupportProperties.isEnabled()) {
            logger.info(">>>>>>>>>>>>> 配置备份未启用，使用：{}.enabled 打开 <<<<<<<<<<<<<<", ConfigSupportProperties.CONFIG_PREFIX);
            return;
        }

        if (isCloudConfigLoaded(propertySources)) {
            // 可以从 spring cloud 中获取配置信息
            PropertySource cloudConfigSource = getLoadedCloudPropertySource(propertySources);
            logger.info(">>>>>>>>>>>> 获取 config service 配置资源 <<<<<<<<<<<<<<<");
            Map<String, Object> backupPropertyMap = makeBackupPropertySource(cloudConfigSource);
            doBackup(backupPropertyMap, configSupportProperties.getFallbackLocation());
        } else {
            logger.info(">>>>>>>>>>>>>> 获取 config Server 资源配置失败 <<<<<<<<<<<<<");
            // 不能获取配置信息，从本地读取
            Properties backupProperty = loadBackupProperty(configSupportProperties.getFallbackLocation());
            if (backupProperty != null) {
                Map backupSourceMap = new HashMap<>(backupProperty);
                PropertySource backupSource = new MapPropertySource("backupSource", backupSourceMap);

                propertySources.addFirst(backupSource);
            }
        }

    }


    @Override
    public int getOrder() {
        return orderNumber;
    }


    /**
     * 从本地加载配置
     */
    private Properties loadBackupProperty(String fallbackLocation) {
        logger.info(">>>>>>>>>>>> 正在从本地加载！<<<<<<<<<<<<<<<<<");
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties properties = new Properties();
        try {
            FileSystemResource fileSystemResource = new FileSystemResource(fallbackLocation);
            propertiesFactoryBean.setLocation(fileSystemResource);
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
            if (properties != null){
                logger.info(">>>>>>>>>>>>>>> 读取成功！<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return properties;
    }


    /**
     * 备份配置信息
     */
    private void doBackup(Map<String, Object> backupPropertyMap, String fallbackLocation) {
        FileSystemResource fileSystemResource = new FileSystemResource(fallbackLocation);
        File file = fileSystemResource.getFile();
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            if (!file.canWrite()){
                logger.info(">>>>>>>>>>>> 文件无法写入：{} <<<<<<<<<<<<<<<", fileSystemResource.getPath());
                return;
            }
            Properties properties = new Properties();
            Iterator<String> iterator = backupPropertyMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                properties.setProperty(key, String.valueOf(backupPropertyMap.get(key)));
            }
            FileOutputStream fileOutputStream = new FileOutputStream(fileSystemResource.getFile());
            properties.store(fileOutputStream, "backup cloud config");
        }catch (Exception e){
            logger.info(">>>>>>>>>> 文件操作失败！ <<<<<<<<<<<");
            e.printStackTrace();
        }
    }

    /**
     * 将配置信息转换为 map
     */
    private Map<String, Object> makeBackupPropertySource(PropertySource cloudConfigSource) {
        Map<String, Object> backupSourceMap = new HashMap<>();
        if (cloudConfigSource instanceof CompositePropertySource) {
            CompositePropertySource propertySource = (CompositePropertySource) cloudConfigSource;
            for (PropertySource<?> source : propertySource.getPropertySources()) {
                if (source instanceof MapPropertySource){
                    MapPropertySource mapPropertySource = (MapPropertySource) source;
                    String[] propertyNames = mapPropertySource.getPropertyNames();
                    for (String propertyName : propertyNames) {
                        if (!backupSourceMap.containsKey(propertyName)) {
                            backupSourceMap.put(propertyName, mapPropertySource.getProperty(propertyName));
                        }
                    }
                }
            }
        }

        return backupSourceMap;
    }


    /**
     * config server 管理配置是否开启
     */
    private boolean isHasCloudConfigLocator(List<PropertySourceLocator> propertySourceLocators) {
        for (PropertySourceLocator propertySourceLocator : propertySourceLocators) {
            if (propertySourceLocator instanceof ConfigServicePropertySourceLocator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取 config service 配置资源
     */
    private PropertySource getLoadedCloudPropertySource(MutablePropertySources propertySources) {
        if (!propertySources.contains(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME)){
            return null;
        }
        PropertySource<?> propertySource = propertySources.get(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME);
//        PropertySource<?> propertySource = propertySources.get("defaultProperties");
        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> source : ((CompositePropertySource) propertySource).getPropertySources()) {
                if ("configService".equals(source.getName())){
                    return source;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否可以从 spring cloud 中获取配置信息
     */
    private boolean isCloudConfigLoaded(MutablePropertySources propertySources) {
        return getLoadedCloudPropertySource(propertySources) != null;
    }
}
